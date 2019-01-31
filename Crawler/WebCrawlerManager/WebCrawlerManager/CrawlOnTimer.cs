using System;
using System.Collections.Generic;
using System.Threading;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Host;

namespace WebCrawlerManager
{
    public static class CrawlOnTimer
    {
        public static bool debug = false;
        public static TraceWriter logs;
        [FunctionName("CrawlOnTimer")]
        public static void Run([TimerTrigger("0 0 12 * * SUN")]TimerInfo myTimer, TraceWriter log, CancellationToken token)
        {
           
            logs = log;
            log.Info($"Bite C# Timer trigger function executed at: {DateTime.Now}");
            DBHandler db = new DBHandler();
            SQLHandler sql = new SQLHandler();
            List<IWebCrawler> crawlers = new List<IWebCrawler>();

            crawlers.Add(new RbnorWebCrawler());
            crawlers.Add(new DustLoopCrawler());
            log.Info($"Start Crawl");
            foreach (IWebCrawler crawler in crawlers)
            {
                crawler.Crawl();
                log.Info($"Crawl Done");
            }
        }
    }
}
