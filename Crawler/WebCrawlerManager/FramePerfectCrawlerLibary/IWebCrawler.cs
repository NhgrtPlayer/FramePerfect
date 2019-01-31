using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FramePerfectCrawlerLibary
{
    public interface IWebCrawler
    {
        List<Character> characters { get; set; }
        HtmlWeb web { get; set; }
        List<Character> Crawl();
    }
}
