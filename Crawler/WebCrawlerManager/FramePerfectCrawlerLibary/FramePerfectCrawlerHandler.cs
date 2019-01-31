using System;
using System.Collections.Generic;
using System.Text;

namespace FramePerfectCrawlerLibary
{
    public class FramePerfectCrawlerHandler
    {
        public List<IWebCrawler> GetCharacterFromCrawlers()
        {
            List<IWebCrawler> crawlers = new List<IWebCrawler>();
            //ADD your crawler in this list
            crawlers.Add(new RbnorWebCrawler());
            crawlers.Add(new DustLoopCrawler());
            return crawlers;
        }
    }
}
