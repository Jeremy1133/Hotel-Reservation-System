package project;

public class storeInfo {
	
	private static Guest guest;
	private static Reservation reserve;
	
	public static void setGuest(Guest jill)
	{
		guest = jill;
	}

	public static Guest getGuest()
	{
		return guest;
	}

	public static Reservation getReserve() {
		return reserve;
	}

	public static void setReserve(Reservation reserve) {
		storeInfo.reserve = reserve;
	}
	
}
