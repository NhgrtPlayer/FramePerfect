using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HtmlAgilityPack;

namespace WebCrawlerManager
{
    class RbnorWebCrawler : IWebCrawler
    {
        string url = "http://rbnorway.org/";
        string startPage = "t7-frame-data/";
        string game = "Tekken7";
        int gameID;
        List<Character> characters = new List<Character>();
        public void Crawl()
        {
            var html = @url + startPage;
            HtmlWeb web = new HtmlWeb();
            var htmlDoc = web.Load(html);
            int nb = 4;

            List<string> charactersUrls = new List<string>();
            var node = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='post-1378']/div/p[" + nb + "]/a");
            while (node != null)
            {
                Console.WriteLine(node.Attributes["href"].Value);
                if (node.Attributes["href"].Value.Contains("http") == false)
                    charactersUrls.Add(url + node.Attributes["href"].Value);
                else
                    charactersUrls.Add(node.Attributes["href"].Value);
                nb++;
                node = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='post-1378']/div/p[" + nb + "]/a");
            }
            foreach (string cUrl in charactersUrls)
            {
                Character character = new Character();
                character.GameName = game;
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
                    Console.WriteLine("Start:\n" + table.Name);
                    foreach (var tr in table.Descendants("tr"))
                    {
                        Console.WriteLine(tr.Name);
                        Move move = new Move();
                        move.MoveType = moveType;
                        int tdNb = 0;
                        foreach (var td in tr.Descendants("td"))
                        {
                            if (td.InnerText.Contains("Command") == true)
                                break;
                            Console.WriteLine(td.InnerText);
                            switch (tdNb)
                            {
                                case 0:
                                    move.Command = td.InnerText;
                                    break;
                                case 1:
                                    move.HitLevel = td.InnerText;
                                    break;
                                case 2:
                                    move.Damage = td.InnerText;
                                    break;
                                case 3:
                                    move.StartUpFrame = td.InnerText;
                                    break;
                                case 4:
                                    move.BlockFrame = td.InnerText;
                                    break;
                                case 5:
                                    move.HitFrame = td.InnerText;
                                    break;
                                case 6:
                                    move.CounterHitFrame = td.InnerText;
                                    break;
                            }
                            tdNb++;
                        }
                        if (move.Command != null)
                            character.Moves.Add(move);
                    }
                    moveType = "Basic";
                    Console.WriteLine("\nEND");
                }
                SQLHandler.Instance.CheckGameID(character);
                characters.Add(character);
            }
            SQLHandler.Instance.ActualiseCharacters(characters);
        }
    }
}
