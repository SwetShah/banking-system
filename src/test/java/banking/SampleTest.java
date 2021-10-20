package banking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import banking.Account;
import banking.Bank;
import banking.Company;
import banking.Person;
import banking.Transaction;

public class SampleTest {
	/**
	 * The bank/
	 */
	Bank bank;
	/**
	 * The account number for Jofra Waughan
	 */
	Long jofraWaughan;
	/**
	 * The account number for Ashley Warne
	 */
	Long ashleyWarne;
	/**
	 * The account number for Kevin Bell
	 */
	Long kevinBell;
	/**
	 * The account number for Marcus
	 */
	Long marcusMaroon;

	@Before
	public void setUp() throws Exception {
		bank = new Bank();
		Person person1jofraWaughan = new Person("Jofra", "Waughan", 1);
		Person person2ashleyWarne = new Person("Ashley", "Warne", 2);
		Company company1kevinBell = new Company("Kevin Bell", 1);
		Company company2marcusMaroon = new Company("Marcus", 2);
		jofraWaughan = bank.openConsumerAccount(person1jofraWaughan, 1111, 0.0);
		ashleyWarne = bank.openConsumerAccount(person2ashleyWarne, 2222, 456.78);
		kevinBell = bank.openCommercialAccount(company1kevinBell, 1111, 0.0);
		marcusMaroon = bank.openCommercialAccount(company2marcusMaroon, 2222, 9876543.21);
	}

	@After
	public void tearDown() throws Exception {
		bank = null;
		jofraWaughan = null;
		ashleyWarne = null;
		kevinBell = null;
		marcusMaroon = null;
	}

	@Test
	public void invalidAccountNumberTest() {
		Assert.assertTrue("1st and 2nd accounts were not assigned sequential account numbers.",
				jofraWaughan + 1 == ashleyWarne);
		Assert.assertTrue("2nd and 3rd accounts were not assigned sequential account numbers.",
				ashleyWarne + 1 == kevinBell);
		Assert.assertTrue("3rd and 4th accounts were not assigned sequential account numbers.",
				kevinBell + 1 == marcusMaroon);

		assertEquals(bank.getBalance(jofraWaughan), 0.0, 0);
		assertEquals(bank.getBalance(ashleyWarne), 456.78, 0);
		assertEquals(bank.getBalance(kevinBell), 0.0, 0);
		assertEquals(bank.getBalance(marcusMaroon), 9876543.21, 0);
		Assert.assertNotEquals(bank.getBalance(jofraWaughan), bank.getBalance(ashleyWarne));
		Assert.assertNotEquals(bank.getBalance(kevinBell), bank.getBalance(marcusMaroon));
	}

	/**
	 * Debit an account.
	 */
	@Test
	public void debitTest() {
		double amount = 20.0;
		assertFalse("Account " + jofraWaughan + " should have insufficient funds.", bank.debit(jofraWaughan, amount));
		assertTrue("Account " + ashleyWarne + " should have sufficient funds.", bank.debit(ashleyWarne, amount));
		assertFalse("Account " + kevinBell + " should have insufficient funds.", bank.debit(kevinBell, amount));
		assertTrue("Account " + marcusMaroon + " should have sufficient funds.", bank.debit(marcusMaroon, amount));
	}

	/**
	 * Test crediting accounts inside {@link Bank}.
	 */
	@Test
	public void bankCreditTest() {
		double amount = 23.45;
		double beforeDeposit1 = bank.getBalance(jofraWaughan);
		double beforeDeposit2 = bank.getBalance(ashleyWarne);
		double beforeDeposit3 = bank.getBalance(kevinBell);
		double beforeDeposit4 = bank.getBalance(marcusMaroon);
		bank.credit(jofraWaughan, amount);
		bank.credit(ashleyWarne, amount);
		bank.credit(kevinBell, amount);
		bank.credit(marcusMaroon, amount);
		assertEquals(beforeDeposit1 + amount, bank.getBalance(jofraWaughan), 0);
		assertEquals(beforeDeposit2 + amount, bank.getBalance(ashleyWarne), 0);
		assertEquals(beforeDeposit3 + amount, bank.getBalance(kevinBell), 0);
		assertEquals(beforeDeposit4 + amount, bank.getBalance(marcusMaroon), 0);
	}

	/**
	 * Tests {@link Transaction}: an attempt to access an account with an invalid PIN must throw an
	 * Exception.
	 *
	 * @throws Exception
	 *             Account validation failed.
	 */
	@Test(expected = Exception.class)
	public void invalidPinTransaction() throws Exception {
		new Transaction(bank, jofraWaughan, 1234);
	}

	/**
	 * Tests {@link Transaction}
	 *
	 * @throws Exception
	 *             Account validation failed.
	 */
	@Test
	public void transactionTest() throws Exception {
		Transaction transaction1 = new Transaction(bank, jofraWaughan, 1111);
		double beforeDeposit1 = transaction1.getBalance();
		double amount = 23452.43;
		transaction1.credit(amount);
		assertEquals(beforeDeposit1 + amount, transaction1.getBalance(), 0);
		assertTrue("Debit was unsuccessful.", transaction1.debit(amount));
		assertFalse("This transaction should have overdrawn the account.", transaction1.debit(amount));
		assertEquals(beforeDeposit1, transaction1.getBalance(), 0);
		assertEquals(transaction1.getBalance(), bank.getBalance(jofraWaughan), 0);
	}
}
