package nhom2.qlsv;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "HelloAppEngine",
		urlPatterns = {"/hello"}
		)
public class HelloAppEngine extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		String list[] = {"bitcoin","ethereum","ripple"};
		response.getWriter().printf("Note: Check since 3 days \n");
		for (int i=0; i <3;i++) {
			String kind = list[i];
			Query<Entity> query = Query.newEntityQueryBuilder().setKind(kind)
					.setOrderBy(StructuredQuery.OrderBy.desc("time")).setLimit(10).build();
			QueryResults<Entity> results = datastore.run(query);

			response.getWriter().printf("%s \n", kind);
			double total[] = new double[10]; // check exception later
			int count=0;
			while (results.hasNext()) {
				Entity ret = results.next();
				String volume24h="24h_volume_usd";
				response.getWriter().printf("Time: %s ::Volume24h: %s \n",ret.getTimestamp("time"), ret.getString(volume24h));
				total[count] = Double.parseDouble(ret.getString(volume24h));
			}
			boolean check=true;
			if (count > 3) count=3;
			for (int j=0; j<count;j++) 
			{
				if (total[j] < total[j+1]) check = false;
			}
			if (check) {
				response.getWriter().printf("=====> Volume is increasing!!! Buy it $$$$$$ \n");
			} else {
				response.getWriter().printf("=====> Volume is not stable!!! Keep your money safe!!!\n");
			}
		}
	}
}

