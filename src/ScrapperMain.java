
public class ScrapperMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScrapperService scrapper = new ScrapperService();
		System.out.println("START SCRAPPING...");
		scrapper.scrapping(1);
		System.out.println("FINISH SCRAPPING");
		System.exit(0);
	}

}
