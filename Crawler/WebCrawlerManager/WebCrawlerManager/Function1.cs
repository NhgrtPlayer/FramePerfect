using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.Azure.WebJobs.Host;
using System.Data.SqlClient;
using System.Collections.Generic;

namespace WebCrawlerManager
{
    public static class Function1
    {
        public static bool debug = false;
        [FunctionName("Function1")]
        public static async Task<HttpResponseMessage> Run([HttpTrigger(AuthorizationLevel.Function, "get", "post", Route = null)]HttpRequestMessage req, TraceWriter log)
        {
            DBHandler db = new DBHandler();
            SQLHandler sql = new SQLHandler();
            List<IWebCrawler> crawlers = new List<IWebCrawler>();

            crawlers.Add(new RbnorWebCrawler());

            foreach (IWebCrawler crawler in crawlers)
                crawler.Crawl();

            log.Info("C# HTTP trigger function processed a request.");
            return req.CreateResponse(HttpStatusCode.OK, "Crawl OK");
            req.CreateResponse(HttpStatusCode.BadRequest, "Crawl OK");

            log.Info("C# HTTP trigger function processed a request.");

            // parse query parameter
            string name = req.GetQueryNameValuePairs()
                .FirstOrDefault(q => string.Compare(q.Key, "name", true) == 0)
                .Value;

            if (name == null)
            {
                // Get request body
                dynamic data = await req.Content.ReadAsAsync<object>();
                name = data?.name;
            }

            return name == null
                ? req.CreateResponse(HttpStatusCode.BadRequest, "Please pass a name on the query string or in the request body:"  + db.OpenConnection()
                + ":" + db.CloseConnection())
                : req.CreateResponse(HttpStatusCode.OK, "Hello " + name);
        }
    }
}
