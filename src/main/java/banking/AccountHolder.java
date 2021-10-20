package banking;

public abstract class AccountHolder {
	private int idNumber = 0;

	/**
	 * @param idNumber The government-issued ID used during account setup.
	 */
	public AccountHolder(int idNumber) {
		this.idNumber += 1;
	}

	/**
	 * @return private int {@link AccountHolder#idNumber}
	 */
	public int getIdNumber() {
		return idNumber;
	}
}
