package fi.utu.tech.assignment5;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App5 {
    // Huom! Main-metodiin ei pitäisi tarvita tehdä muutoksia!
    // Main-metodi ainoastaan luo tilit ja alkaa tekemään samanaikaisia tilisiirtoja
    // sekä lopuksi varmistaa, että laittomuuksia ei tapahtunut
    public static void main(String[] args) {
        System.out.println("Press Ctrl+C to terminate");
        Random rnd = new Random();

        // Montako tiliä luodaan
        int accountCount = 40;

        // Montako tilisiirtoa tehdään
        int transfers = 100;


        // Luodaan tilit ja annetaan starttirahaa
        Account[] accounts = new Account[accountCount];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(rnd.nextInt());
            accounts[i].deposit(rnd.nextDouble(500));
        }
        System.out.println("=========== Tilit luotu ===========");
        // ExecutorService tilisiirtojen suorittamista varten. Blokkaa, mikäli jono tulee täyteen.
        ExecutorService transferExecutor = new ThreadPoolExecutor(40,
                40,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(50),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // Tehdään ja suoritetaan tilisiirtotapahtumia niin kauan kunnes ohjelma suljetaan
        for (int i = 0; i < transfers; i++) {
            var from = accounts[rnd.nextInt(accounts.length)];
            var to = accounts[rnd.nextInt(accounts.length)];
            var bt = new BankTransfer(from, to, rnd.nextDouble(500.0));
            transferExecutor.execute(bt);
        }

        /* 
         * Alapuolella tarkastetaan tilien loppusaldot, jotta
         * saadaan vihiä ratkaisun toimivuudesta.
         * Huom! Kilpailutilanteet johtuen luonteestaan,
         * eivät tapahdu joka ajokerralla.
         */

         try {
            transferExecutor.shutdown();
            transferExecutor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ie) {
            System.out.println("Interrupted!");
        }

        System.out.println("------ KAIKKI TILISIIRROT TEHTY ------");
        for (int k = 0; k < accounts.length; k++) {
            if (accounts[k].getBalance() <= 0 || accounts[k].getBalance() > 1000) {
                System.out.printf("Tilillä %d laiton balanssi: %f %n", accounts[k].accountNumber,
                        accounts[k].getBalance());
            }
        }


    }

}

/**
 * Tilisiirtoa kuvaava luokka
 */
class BankTransfer implements Runnable {

    private Account from;
    private Account to;
    private double amount;
    private Random rnd;


    /**
     * 
     * @param from Tili, jolta siirretään
     * @param to Tili, jolle siirretään
     * @param amount Rahamäärä, joka siirretään
     */
    public BankTransfer(Account from, Account to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.rnd = new Random();
    }

    /**
     * Tilisiirron suorittava metodi. Lukitsee lähde- ja kohdetilin säikeelle eksklusiivisesti.
     */
    @Override
    public void run() {
        // Lukitan 1. tili
        synchronized (from) {
            // Lukko ensimmäiseen tiliin saatu, aloitetaan toisen tilin lukitus
            synchronized (to) {
                // Säie sai yksinoikeudet molempiin tileihin, tarkistetaan tilien kate ja suoritetaan siirto,
                // jos lakiehdot täyttyvät
                if ((from.getBalance() - amount) > 0 && (to.getBalance() + amount) <= 1000) {
                    /*
                     * Jos tässä kohtaa toinen siirtotapahtuma tekisi siirron, siirto voisi mennä
                     * yli lain rajojen kilpailutilanteen sattuessa. Lukot estävät sen tällä hetkellä.
                     * Alla oleva sleep tekee kilpailutilanteista todennäköisempiä, siltä varalta,
                     * että ratkaisunne aiheuttaa niitä. 
                     * Sleepin poistaminen vähentää kilpailutilanteen riskiä, mutta
                     * ei silti poista sen teoreettista mahdollisuutta ilmaantua.
                     */ 
                    try {Thread.sleep(rnd.nextInt(500));} catch (InterruptedException ie) {}
                    // Tehdään aktuaalinen tilisiirto
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            }
        }
        // Tilisiirto suoritettu ja lukot avattu
    }

}

/**
 * Pankkitiliä kuvaava luokka
 */
class Account implements Comparable<Account> {

    private double balance = 0.0;
    public final int accountNumber;

    /**
     * Pankkitilin konstruktori
     * @param accountNumber Pankkitilin tilinumero
     */
    public Account(int accountNumber) {
        this.accountNumber = Math.abs(accountNumber);
    }

    /**
     * Nosta rahaa tililtä
     * @param amount Nostettava rahamäärä
     */
    public synchronized void withdraw(double amount) {
        System.out.printf("Withdrawing %f from %d%n", amount, accountNumber);
        balance -= amount;
    }

    /**
     * Pane rahaa tilille
     * @param amount Pantava rahamäärä
     */
    public synchronized void deposit(double amount) {
        System.out.printf("Depositing %f to %d%n", amount, accountNumber);
        balance += amount;
    }

    /**
     * Saldotiedustelu
     * @return Pankkitilin tämänhetkinen saldo
     */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Vertaile pankkitilejä toisiinsa. Vertailun tulos perustuu tilinumeroon, EI saldoon.
     * @param other Toinen tili, johon tätä tiliä verrataan
     * @return -1, 0 tai 1, jos tämän tilin tilinumero on toisen tilin tilinumeroa pienempi, yhtäsuuri tai suurempi
     */
    @Override
    public int compareTo(Account other) {
        if (this.accountNumber == other.accountNumber)
            return 0;
        else if (this.accountNumber > other.accountNumber)
            return 1;
        else
            return -1;
    }

}