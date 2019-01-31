using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using FramePerfectCrawlerLibary;

namespace TestCrawler
{
    class Program
    {
        public static bool debug = false;
        static void Main(string[] args)
        {
            DBHandler db = new DBHandler();
            SQLHandler sql = new SQLHandler();
            foreach (IWebCrawler crawler in new FramePerfectCrawlerHandler().GetCharacterFromCrawlers())
              sql.ActualiseCharacters(crawler.Crawl());

        }
    }
}
