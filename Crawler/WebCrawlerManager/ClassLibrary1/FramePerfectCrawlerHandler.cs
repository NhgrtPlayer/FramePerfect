using System;
using System.Collections.Generic;
using System.Text;

namespace FramePerfectCrawlerLib
{
    class FramePerfectCrawlerHandler
    {
        public List<IWebCrawler> GetCharacterFromCrawlers()
        {
            List<IWebCrawler> crawlers = new List<IWebCrawler>();
            crawlers.Add(new RbnorWebCrawler());
            crawlers.Add(new DustLoopCrawler());
            return crawlers;
        }
    }
}
