using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FramePerfectCrawlerLibary
{
    public enum MoveValue
    {
        None,
        Command,
        Damage,
        StartUp,
        Active,
        Recovery,
        FrameAdvantage,
        Guard,
        MoveType
    }
    public class DustLoopCrawler : IWebCrawler
    {
        string url = "http://www.dustloop.com";
        List<string> frameDataUrls = new List<string>()
        {
            "/wiki/index.php?title=BBCF/Frame_Data",
            "/wiki/index.php?title=GGACR/Frame_Data",
            "/wiki/index.php?title=P4AU/Frame_Data",
            "/wiki/index.php?title=GGXRD-R2/Frame_Data",
            "/wiki/index.php?title=DBFZ/Frame_Data",
            "/wiki/index.php?title=BBTag/Frame_Data"
        };
        List<string> games = new List<string>()
        {
            "BlazBlue: Central Fiction",
            "Guilty Gear XX Accent Core + R",
            "Persona 4: Arena Ultimax",
            "Guilty Gear Xrd REV 2",
            "Dragon Ball FighterZ",
            "BlazBlue Cross Tag Battle"
        };

        public HtmlWeb web { get; set; } = new HtmlWeb();
        public List<Character> characters { get; set; } = new List<Character>();
        public List<Character> Crawl()
        {
            for (int gameIdx = 0; gameIdx < games.Count; gameIdx++)
            {
                
                var html = url + frameDataUrls[gameIdx];
                web = new HtmlWeb();
                var htmlDoc = web.Load(html);
                List<string> charactersUrls = new List<string>();
                var nodeDiv = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='mw-content-text']/div/div[2]");
                if (nodeDiv == null)
                    nodeDiv = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='mw-content-text']/div/div");
                var nodePs = nodeDiv.Descendants("p");
                foreach (var p in nodePs)
                    foreach (var a in p.Descendants("a"))
                        charactersUrls.Add(a.Attributes["href"].Value);
                foreach (string characterUrl in charactersUrls)
                {
                    html = url + characterUrl;
                    htmlDoc = web.Load(html);
                    Character character = new Character();
                    character.GameName = games[gameIdx];
                    var imgNode = htmlDoc.DocumentNode.Descendants("img");
                    string name = "";
                    if (characterUrl.Contains("/Frame_Data") == true)
                        name = characterUrl.Substring(characterUrl.IndexOf("title"), characterUrl.IndexOf("/Frame_Data") - characterUrl.IndexOf("title"));
                    else if (characterUrl.Contains("_Frame_Data") == true)
                        name = characterUrl.Substring(characterUrl.IndexOf("title"), characterUrl.IndexOf("_Frame_Data") - characterUrl.IndexOf("title"));
                    character.Name = name.Substring(name.IndexOf("/") + 1, name.Length - name.IndexOf("/") - 1);
                    string charShortName;
                    if (character.Name.Contains("%27") == true)
                        character.Name = character.Name.Replace("%27", "_");
                    if (character.Name.Contains("_") == true)
                        charShortName = character.Name.Substring(0, character.Name.IndexOf("_"));
                    else
                        charShortName = character.Name;
                    foreach (var img in imgNode)
                    {
                        if (img.Attributes["src"].Value.Contains(charShortName) == true)
                        {
                            character.ImgUrl = url + img.Attributes["src"].Value;
                            break;
                        }
                    }
                    character.Name = character.Name.Replace('_', ' ');
                    Console.WriteLine("CharacterName:" + character.Name);
                    var nodeTables = htmlDoc.DocumentNode.SelectSingleNode("//*[@id='mw-content-text']/div").Descendants("table");
                    int tableNb = 0;
                    foreach (HtmlNode table in nodeTables)
                    {
                        Console.WriteLine("GameIDx:" + games[gameIdx] + ":" + character.Name + ":" + tableNb + ":" + nodeTables.Count() + ":" + characters.Count);
                        var moveTypeNode = table.PreviousSibling;
                        Console.WriteLine("MoveTypeNode:" + moveTypeNode.InnerText);
                        while (moveTypeNode != null && moveTypeNode.Name.IndexOf("h2") < 0)
                        {
                            moveTypeNode = moveTypeNode.PreviousSibling;
                            //Console.WriteLine("MT:" + moveTypeNode.Name);
                        }
                        string moveType = "";
                        if (moveTypeNode != null)
                        {
                            moveTypeNode = moveTypeNode.Descendants("span").First();
                            moveType = moveTypeNode.InnerText;
                            Console.WriteLine("MoveType:" + moveType + ":" + ":" + moveTypeNode.Name + ":" + moveTypeNode.XPath);
                        }
                        //                        moveType = moveType.Substring(0, moveType.IndexOf("[edit]"));
                        int loopNb = 0;
                        List<MoveValue> tabMoveCorrespondance = new List<MoveValue>();
                        Console.WriteLine("TrNb:" + table.Descendants("tr").Count());
                        foreach (var tr in table.Descendants("tr"))
                        {
                            if (loopNb == 0)
                            {
                                if (tr.Descendants("th") != null &&
                                    tr.Descendants("th").Count() == 1)
                                    continue;
                                foreach (var th in tr.Descendants("th"))
                                {
                                    //Console.WriteLine(th.Name + ":" + th.XPath);
                                    if (th.FirstChild != null)
                                    {
                                        tabMoveCorrespondance.Add(MatchWithMoveValues(th.FirstChild.InnerText));
                                    }
                                    else
                                        tabMoveCorrespondance.Add(MatchWithMoveValues(th.InnerText));
                                }
                            }
                            else if (tabMoveCorrespondance.Count > 0)
                            {
                                Move move = new Move();
                                move.MoveType = moveType;
                                int descNb = 0;
                                foreach (var desc in tr.Descendants())
                                {
                                    if (desc.Name.Contains("th"))
                                        AssignMoveValue(move, desc.InnerText, tabMoveCorrespondance[descNb++]);
                                    if (desc.Name.Contains("td"))
                                        AssignMoveValue(move, desc.InnerText, tabMoveCorrespondance[descNb++]);
                                }
                                character.Moves.Add(move);
                            }

                            loopNb++;
                        }
                        tableNb++;
                    }
                    characters.Add(character);
                }
            }
            return characters;
        }
        public MoveValue MatchWithMoveValues(string value)
        {
            if (value.Contains("Name") == true || value.Contains("Version") == true)
                return MoveValue.Command;
            if (value.Contains("Damage") == true)
                return MoveValue.Damage;
            if (value.Contains("Startup") == true)
                return MoveValue.StartUp;
            if (value.Contains("Active") == true)
                return MoveValue.Active;
            if (value.Contains("Recovery") == true)
                return MoveValue.Recovery;
            if (value.Contains("Frame Adv") == true)
                return MoveValue.FrameAdvantage;
            if (value.Contains("Guard") == true)
                return MoveValue.Guard;
            return MoveValue.None;
        }
        public void AssignMoveValue(Move move, string value, MoveValue mValue)
        {
            value = value.Replace("\n", "");
            value = value.Replace("'", " ");
            //Console.WriteLine(value + ":" + mValue);
            switch (mValue)
            {
                case MoveValue.Active:
                    move.Active = value;
                    break;
                case MoveValue.Command:
                    move.Command = value;
                    break;
                case MoveValue.Damage:
                    move.Damage = value;
                    break;
                case MoveValue.FrameAdvantage:
                    move.FrameAdvantage = value;
                    break;
                case MoveValue.Guard:
                    move.Guard = value;
                    break;
                case MoveValue.Recovery:
                    move.Recovery = value;
                    break;
                case MoveValue.StartUp:
                    move.StartUp = value;
                    break;
                case MoveValue.None:
                    break;
            }
        }
    }
}
