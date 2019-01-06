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
            Console.WriteLine("CheckGameID for " + character.Name);
            List<string>[] dbChars = DBHandler.InstanceDb.Select("SELECT ID FROM Games where Name = '"
                    + character.GameName + "'", 1);
            foreach (List<string> list in dbChars)
                foreach (string s in list)
                    Console.WriteLine(s);
            if (dbChars[0] != null && dbChars[0].Count >= 1)
                character.GameID = dbChars[0][0];
            else
            {
                string query = "INSERT INTO Games (Name) VALUES ('" + character.GameName + "')";
                DBHandler.InstanceDb.ExectuteNoQuery(query);
                CheckGameID(character);
            }
        }

        public void ActualiseCharacters(List<Character> characters)
        {
            foreach (Character character in characters)
            {
                List<string>[] dbChars = DBHandler.InstanceDb.Select("SELECT Characters.ID, Characters.GameID FROM Characters, Games where Characters.Name = '"
                    + character.Name +
                    "' AND Games.Name = '" + character.GameName + "'", 2);
                foreach (List<string> list in dbChars)
                    foreach (string s in list)
                        Console.WriteLine(s);
                if (dbChars[0] != null && dbChars[0].Count >= 2)
                {
                    Console.WriteLine(character.Name + " Exist!");
                    character.ID = dbChars[0][0];
                    character.GameID = dbChars[0][1];
                    foreach (Move move in character.Moves)
                    {
                        move.CharacterID = character.ID;
                        string query = "UPDATE Move SET HitLevel = '" + move.HitLevel +
                            "',Damage = '" + move.Damage +
                            "',StartUpFrame = '" + move.StartUpFrame +
                            "',BlockFrame = '" + move.BlockFrame +
                            "',HitFrame = '" + move.HitFrame +
                            "',CounterHitFrame = '" + move.CounterHitFrame +
                            "',MoveType = '" + move.MoveType +
                            "' WHERE Characters.ID = '" + move.CharacterID + "' AND Command = '" + move.Command + "'";
                    }
                }
                else
                {
                    Console.WriteLine(character.Name + " Don't exist");
                    CheckGameID(character);
                    string query = "INSERT INTO Characters (Name,GameID) VALUES ('" + character.Name + "','" + character.GameID + "')";
                    DBHandler.InstanceDb.ExectuteNoQuery(query);
                    dbChars = DBHandler.InstanceDb.Select("SELECT Characters.ID, GameID FROM Characters, Games where Characters.Name = '"
                   + character.Name +
                   "' AND Games.Name = '" + character.GameName + "'", 2);
                    foreach (List<string> list in dbChars)
                        foreach (string s in list)
                            Console.WriteLine(s);
                    if (dbChars[0] != null && dbChars[0].Count >= 1)
                    {
                        Console.WriteLine(character.Name + " Exist!");
                        character.ID = dbChars[0][0];
                        foreach (Move move in character.Moves)
                        {
                            move.CharacterID = character.ID;
                            query = "INSERT INTO Moves (CharacterID, Command, HitLevel, Damage, StartUpFrame, BlockFrame, HitFrame, CounterHitFrame, MoveType) VALUES ('"
                                + move.CharacterID +
                                "','" + move.Command +
                                "','" + move.HitLevel +
                                "','" + move.Damage +
                                "','" + move.StartUpFrame +
                                "','" + move.BlockFrame +
                                "','" + move.HitFrame +
                                "','" + move.CounterHitFrame +
                                "','" + move.MoveType + "')";
                            DBHandler.InstanceDb.ExectuteNoQuery(query);
                        }
                    }
                    else
                    {
                        Console.WriteLine(character.Name + " Don't exist, fail to insert character");
                    }
                }
            }
        }
    }
}
