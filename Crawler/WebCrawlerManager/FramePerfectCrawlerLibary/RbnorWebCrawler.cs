using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HtmlAgilityPack;

namespace FramePerfectCrawlerLibary
{
    public class RbnorWebCrawler : IWebCrawler
    {
        string url = "http://rbnorway.org/";
        string startPage = "t7-frame-data/";
        string game = "Tekken7";

        public HtmlWeb web { get; set; } = new HtmlWeb();
        public List<Character> characters { get; set; } = new List<Character>();
        
        public List<Character> Crawl()
        {
            var html = @url + startPage;
            var htmlDoc = web.Load(html);
            int nb = 4;

            List<string> charactersUrls = new List<string>();
            List<string> charactersImgUrls = new List<string>();
            var node = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='post-1378']/div/p[" + nb + "]/a");
            while (node != null)
            {
                Console.WriteLine(node.Attributes["href"].Value);
                if (node.Attributes["href"].Value.Contains("http") == false)
                    charactersUrls.Add(url + node.Attributes["href"].Value);
                else
                    charactersUrls.Add(node.Attributes["href"].Value);
                charactersImgUrls.Add(node.Descendants("img").First().Attributes["src"].Value);
                nb++;
                node = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='post-1378']/div/p[" + nb + "]/a");
            }
            int charNb = 0;
            foreach (string cUrl in charactersUrls)
            {
                Character character = new Character();
                character.GameName = game;
                character.ImgUrl = charactersImgUrls[charNb++];
                Console.WriteLine(cUrl);
                htmlDoc = web.Load(@cUrl);
                string postIdString = htmlDoc.DocumentNode.SelectSingleNode("/html/body").Attributes["class"].Value;
                postIdString = postIdString.Substring(postIdString.IndexOf("page-id"));
                postIdString = postIdString.Substring(postIdString.Length - 4);
                string name = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='post-" + postIdString + "']/h2").InnerText;
                character.Name = name.Substring(0, name.IndexOf(" T7 Frames"));
                string moveType = "Special";
                var tables = htmlDoc.DocumentNode.Descendants("table");
                foreach (var table in tables)
                {
                    //Console.WriteLine("Start:\n" + table.Name);
                    foreach (var tr in table.Descendants("tr"))
                    {
                        //Console.WriteLine(tr.Name);
                        Move move = new Move();
                        move.MoveType = moveType;
                        int tdNb = 0;
                        foreach (var td in tr.Descendants("td"))
                        {
                            if (td.InnerText.Contains("Command") == true)
                                break;
                            //Console.WriteLine(td.InnerText);
                            switch (tdNb)
                            {
                                case 0:
                                    move.Command = td.InnerText;
                                    break;
                                case 1:
                                    move.Guard = td.InnerText;
                                    break;
                                case 2:
                                    move.Damage = td.InnerText;
                                    break;
                                case 3:
                                    move.StartUp = td.InnerText;
                                    break;
                                case 4:
                                    move.FrameAdvantage = td.InnerText;
                                    break;
                                case 5:
                                    move.Active = td.InnerText;
                                    break;
                                case 6:
                                    //move.CounterHitFrame = td.InnerText;
                                    break;
                            }
                            tdNb++;
                        }
                        if (move.Command != null)
                            character.Moves.Add(move);
                    }
                    moveType = "Normal";
                    Console.WriteLine("\nEND");
                }
                characters.Add(character);
            }
            return characters;
        }
    }
}
