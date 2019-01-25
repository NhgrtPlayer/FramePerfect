using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WebCrawlerManager
{
    class SQLHandler
    {
        public static SQLHandler Instance;

        public SQLHandler()
        {
            Instance = this;
        }

        public void CheckGameID(Character character)
        {
            CrawlOnTimer.logs.Info("CheckGameID for " + character.Name);
            List<string>[] dbChars = DBHandler.InstanceDb.Select("SELECT id FROM GameItem where name = '"
                    + character.GameName + "'", 1);
            foreach (List<string> list in dbChars)
                foreach (string s in list)
                    CrawlOnTimer.logs.Info(s);
            if (dbChars[0].Count != 0)
                character.GameID = dbChars[0][0];
            else
            {
                string id = CheckNewIaAviable("GameItem");
                string query = "INSERT INTO GameItem (id,name) VALUES ('" + id + "','" + character.GameName + "')";
                DBHandler.InstanceDb.ExectuteNoQuery(query);
                CheckGameID(character);
            }
            CrawlOnTimer.logs.Info("End Check GameId");
        }

        public void ActualiseCharacters(List<Character> characters)
        {
            CrawlOnTimer.logs.Info("Actualise Char");
            foreach (Character character in characters)
            {
                List<string>[] dbChars = DBHandler.InstanceDb.Select("SELECT CharacterItem.id, CharacterItem.gameId FROM CharacterItem, GameItem where CharacterItem.name = '"
                    + character.Name +
                    "' AND GameItem.name = '" + character.GameName + "'", 2);
                foreach (List<string> list in dbChars)
                {
                    CrawlOnTimer.logs.Info("List count:" + list.Count);
                    foreach (string s in list)
                        CrawlOnTimer.logs.Info(s);
                }
                if (dbChars[0].Count != 0)
                {
                    CrawlOnTimer.logs.Info(character.Name + " Exist!");
                    character.ID = dbChars[0][0];
                    character.GameID = dbChars[1][0];
                    foreach (Move move in character.Moves)
                    {
                        if (CheckNoEmptyMove(move) == true)
                        {
                            move.CharacterID = character.ID;
                            dbChars = DBHandler.InstanceDb.Select("SELECT MoveItem.id FROM MoveItem, CharacterItem where CharacterItem.id = '"
                            + move.CharacterID +
                            "' AND MoveItem.name = '" + move.Command + "'", 1);
                            if (dbChars[0].Count != 0)
                            {
                                move.Id = dbChars[0][0];
                                string query = "UPDATE MoveItem SET guard = '" + move.Guard +
                                    "',damage = '" + move.Damage +
                                    "',startUp = '" + move.StartUp +
                                    "',frameAdvantage = '" + move.FrameAdvantage +
                                    "',active = '" + move.Active +
                                    "',recovery = '" + move.Recovery +
                                    "',moveType = '" + move.MoveType +
                                    "',name = '" + move.Command +
                                    "' WHERE id = '" + move.Id + "'";
                                DBHandler.InstanceDb.ExectuteNoQuery(query);
                            }
                            else
                            {
                                move.CharacterID = character.ID;
                                move.Id = CheckNewIaAviable("MoveItem");
                                string query = "INSERT INTO MoveItem (id, characterId, name, guard, damage, startUp, frameAdvantage, active, recovery, moveType) VALUES ('"
                                    + move.Id +
                                    "','" + move.CharacterID +
                                    "','" + move.Command +
                                    "','" + move.Guard +
                                    "','" + move.Damage +
                                    "','" + move.StartUp +
                                    "','" + move.FrameAdvantage +
                                    "','" + move.Active +
                                    "','" + move.Recovery +
                                    "','" + move.MoveType + "')";
                                DBHandler.InstanceDb.ExectuteNoQuery(query);
                            }
                        }
                    }
                }
                else
                {
                    CrawlOnTimer.logs.Info(character.Name + " Don't exist");
                    CheckGameID(character);
                    character.ID = CheckNewIaAviable("CharacterItem");
                    string query = "INSERT INTO CharacterItem (id,name,gameId,imgUrl) VALUES ('" + character.ID + "','" + character.Name + "','" + character.GameID + "','" + character.ImgUrl + "')";
                    DBHandler.InstanceDb.ExectuteNoQuery(query);
                    dbChars = DBHandler.InstanceDb.Select("SELECT CharacterItem.id, gameId FROM CharacterItem, GameItem where CharacterItem.name = '"
                   + character.Name +
                   "' AND GameItem.name = '" + character.GameName + "'", 2);
                    foreach (List<string> list in dbChars)
                        foreach (string s in list)
                            CrawlOnTimer.logs.Info(s);
                    if (dbChars[0].Count != 0)
                    {
                        CrawlOnTimer.logs.Info(character.Name + " Exist!");
                        foreach (Move move in character.Moves)
                        {
                            if (CheckNoEmptyMove(move) == true)
                            {
                                move.CharacterID = character.ID;
                                move.Id = CheckNewIaAviable("MoveItem");
                                query = "INSERT INTO MoveItem (id, characterId, name, guard, damage, startUp, frameAdvantage, active, recovery, moveType) VALUES ('"
                                    + move.Id +
                                    "','" + move.CharacterID +
                                    "','" + move.Command +
                                    "','" + move.Guard +
                                    "','" + move.Damage +
                                    "','" + move.StartUp +
                                    "','" + move.FrameAdvantage +
                                    "','" + move.Active +
                                    "','" + move.Recovery +
                                    "','" + move.MoveType + "')";
                                DBHandler.InstanceDb.ExectuteNoQuery(query);
                            }
                        }
                    }
                    else
                    {
                        CrawlOnTimer.logs.Info(character.Name + " Don't exist, fail to insert character");
                    }
                }
            }
        }

        public string CheckNewIaAviable(string table)
        {
            bool isIdAviable = false;
            string id = Guid.NewGuid().ToString();
            while (isIdAviable == true)
            {
                List<string>[] dbChars = DBHandler.InstanceDb.Select("SELECT " + table + ".name FROM " + table + " where " + table + ".id = '"
                    + id + "'", 1);
                if (dbChars[0].Count != 0)
                    break;
                id = Guid.NewGuid().ToString();
            }
            return id;
        }
        public bool CheckNoEmptyMove(Move move)
        {
            int emptyNb = 0;
            if (move.Command == null)
                emptyNb += 4;
            if (move.Damage == "")
                emptyNb++;
            if (move.FrameAdvantage == "")
                emptyNb++;
            if (move.StartUp == "")
                emptyNb++;
            if (move.Active == "")
                emptyNb++;
            if (move.Recovery == "")
                emptyNb++;
            if (move.Guard == "")
                emptyNb++;
            if (emptyNb > 3)
                return false;
            return true;
        }
    }
}
