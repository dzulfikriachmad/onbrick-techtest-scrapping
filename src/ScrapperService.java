import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

public class ScrapperService {
	List<Product> products = new ArrayList<>();

	public void scrapping(int page) {
		try {
			Document doc = Jsoup
					.connect("https://www.tokopedia.com/search?ob=5&page=" + page + "&q=Handphone&st=product").get();
			Elements sectionDiv = doc.select("div.css-12sieg3");
			//System.outprintln(sectionDiv.size());
			for (Element e : sectionDiv) {
				if (products.size() > 100) {
					break;
				}
				Element urlDetail = e.select("div.css-1ehqh5q").select("a[href]").first();
				if (urlDetail != null) {
					if (!urlDetail.attr("href").contains("https://ta.tokopedia.com/promo")) {

						Product p = new Product();
						Element name = e.selectFirst("div.css-1f4mp12");
						if (name != null) {
							////System.outprintln(name.text());
							p.setName(name.text());
						}

						Element price = e.selectFirst("div.css-rhd610");
						if (price != null) {
							//System.outprintln(price.text());
							p.setPrice(price.text());
						}

						if (urlDetail != null) {
							//System.outprintln(urlDetail.attr("href"));
//						Response response = Jsoup.connect(urlDetail.attr("href")).followRedirects(true).execute();
//						//System.outprintln(response.url());
							Document detail = Jsoup.connect(urlDetail.attr("href")).userAgent("Mozilla").get();
							Element elDet = detail.selectFirst("div[data-testid=lblPDPDescriptionProduk]");
							if(elDet!=null) {
								//System.outprintln(elDet.text());
								p.setDesc(elDet.text());
							}

						}

						Element image = e.select("div.css-10xc038").select("img[src]").first();
						if (image != null) {
							//System.outprintln(image.attr("src"));
							p.setImgUrl(image.attr("src"));
						}

						Element rating = e.selectFirst("span.css-etd83i");
						if (rating != null) {
							//System.outprintln(rating.text());
							p.setRating(rating.text());
						}

						Element store = e.selectFirst("span.css-qjiozs[data-testid='']");
						if (store != null) {
							//System.outprintln(store.text());
							p.setStore(store.text());
						}
						products.add(p);
					}
				}

			}
			if (products.size() <= 100) {
				scrapping(page + 1);
			} else {
				//System.outprintln(products.size());
				populateCsv();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void populateCsv() {
		try {
			System.out.println("POPULATING DATA TO CSV....");
			String[] headers = {"No.","Name","Description","Image Link","Price","Rating","Store"};
			List<String[]> data = new ArrayList<>();
			data.add(headers);
			if(products.size()>0) {
				int i = 1;
				for(Product p: products) {
					String[] pArr = {String.valueOf(i), p.getName(), p.getDesc(), p.getImgUrl(), p.getPrice(), p.getRating(), p.getStore()};
					data.add(pArr);
					i++;
				}
				
			}
			try (CSVWriter writer = new CSVWriter(new FileWriter("ScrappingData-"+new Date().toString().replace(" ","_").replace(":", "_")+".csv"))) {
	            writer.writeAll(data);
	        }
			System.out.println("FINISH POPULATING DATA TO CSV");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
