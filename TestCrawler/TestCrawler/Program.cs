﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestCrawler
{
    class Program
    {
        public static bool debug = false;
        static void Main(string[] args)
        {
            DBHandler db = new DBHandler();
            SQLHandler sql = new SQLHandler();
            DustLoopCrawler dust = new DustLoopCrawler();
            RbnorWebCrawler rbor = new RbnorWebCrawler();
            rbor.Crawl();
            dust.Crawl();
        }
    }
}
