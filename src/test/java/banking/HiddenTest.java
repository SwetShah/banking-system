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

public class HiddenTest {
	/**
	 * The bank/
	 */
	Bank bank;
	/**
	 * The account number for james.
	 */
	Long james;
	/**
	 * The account number for mark
	 */
	Long mark;

	/**
	 * The account number for bairstow
	 */
	Long bairstow;
	/**
	 * The account number for roy
	 */
	Long roy;
	/**
	 * The account number for alex
	 */
	Long alex;


	@Before
	public void setUp() throws Exception {
		bank = new Bank();
		Person person1james = new Person("james", "plunkett", 1);
		Person person2mark = new Person("mark", "singh", 2);
		Person person3bairstow = new Person("bairstow", "malan", 3);
		Company company1roy = new Company("roy", 1);
		Company company2alex = new Company("alex", 2);
		james = bank.openConsumerAccount(person1james, 1111, 0.0);
		mark = bank.openConsumerAccount(person2mark, 2222, 456.78);
		bairstow = bank.openConsumerAccount(person3bairstow,3333,500.00);
		roy = bank.openCommercialAccount(company1roy, 1111, 0.0);
		alex = bank.openCommercialAccount(company2alex, 2222, 123456789.00);
	}

	@After
	public void tearDown() throws Exception {
		bank = null;
		james = null;
		mark = null;
		bairstow=null;
		roy = null;
		alex = null;
	}

	@Test
	public void invalidAccountNumberTest() {
		Assert.assertTrue("1st and 2nd accounts were not assigned sequential account numbers.",
				james + 1 == mark);
		Assert.assertTrue("2nd and 3rd accounts were not assigned sequential account numbers.",
				mark + 1 == bairstow);
		Assert.assertTrue("3rd and 4th accounts were not assigned sequential account numbers.",
				bairstow + 1 == roy);
		Assert.assertTrue("4rd and 5th accounts were not assigned sequential account numbers.",
				roy + 1 == alex);

		assertEquals(bank.getBalance(james), 0.0, 0);
		assertEquals(bank.getBalance(mark), 456.78, 0);
		assertEquals(bank.getBalance(bairstow), 500.00, 0);
		assertEquals(bank.getBalance(roy), 0.0, 0);
		assertEquals(bank.getBalance(alex), 123456789.00, 0);
		Assert.assertNotEquals(bank.getBalance(james), bank.getBalance(mark));
		Assert.assertNotEquals(bank.getBalance(james), bank.getBalance(bairstow));
		Assert.assertNotEquals(bank.getBalance(roy), bank.getBalance(alex));
	}

	/**
	 * Debit an account.
	 */
	@Test
	public void debitTest() {
		double amount = 200.0;
		assertFalse("Account " + james + " should have insufficient funds.", bank.debit(james, amount));
		assertTrue("Account " + mark + " should have sufficient funds.", bank.debit(mark, amount));
		assertTrue("Account " + bairstow + " should have sufficient funds.", bank.debit(bairstow, amount));
		assertFalse("Account " + roy + " should have insufficient funds.", bank.debit(roy, amount));
		assertTrue("Account " + alex + " should have sufficient funds.", bank.debit(alex, amount));
	}

	/**
	 * Test crediting accounts inside {@link Bank}.
	 */
	@Test
	public void bankCreditTest() {
		double amount = 500.00;
		double beforeDeposit1 = bank.getBalance(james);
		double beforeDeposit2 = bank.getBalance(mark);
		double beforeDeposit3 = bank.getBalance(bairstow);
		double beforeDeposit4 = bank.getBalance(roy);
		double beforeDeposit5 = bank.getBalance(alex);
		bank.credit(james, amount);
		bank.credit(mark, amount);
		bank.credit(bairstow, amount);
		bank.credit(roy, amount);
		bank.credit(alex, amount);
		assertEquals(beforeDeposit1 + amount, bank.getBalance(james), 0);
		assertEquals(beforeDeposit2 + amount, bank.getBalance(mark), 0);
		assertEquals(beforeDeposit3 + amount, bank.getBalance(bairstow),0);
		assertEquals(beforeDeposit4 + amount, bank.getBalance(roy), 0);
		assertEquals(beforeDeposit5 + amount, bank.getBalance(alex), 0);
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
		new Transaction(bank, james, 9999);
	}

	/**
	 * Tests {@link Transaction}
	 *
	 * @throws Exception
	 *             Account validation failed.
	 */
	@Test
	public void transactionTest() throws Exception {
		Transaction transaction1 = new Transaction(bank, bairstow, 3333);
		double beforeDeposit1 = transaction1.getBalance();
		double amount = 1000000.23;
		transaction1.credit(amount);
		assertEquals(beforeDeposit1 + amount, transaction1.getBalance(), 0);
		assertTrue("Debit was unsuccessful.", transaction1.debit(amount));
		assertFalse("This transaction should have overdrawn the account.", transaction1.debit(amount));
		assertEquals(beforeDeposit1, transaction1.getBalance(), 0);
		assertEquals(transaction1.getBalance(), bank.getBalance(bairstow), 0);
	}
	@Test
	public void transactionTest2() throws Exception{
		Transaction t2 = new Transaction(bank, james, 1111);
		double beforeDeposit1 = t2.getBalance();
		double amount = 19239.34;
		t2.credit(amount);
		assertEquals(beforeDeposit1 + amount, t2.getBalance(), 0);
		assertTrue("Debit was unsuccessful.", t2.debit(amount));
		assertEquals(beforeDeposit1, t2.getBalance(), 0);
		assertEquals(t2.getBalance(), bank.getBalance(james), 0);
	}
}
