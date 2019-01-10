using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestCrawler
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
                        string query = "UPDATE Move SET Guard = '" + move.Guard +
                            "',Damage = '" + move.Damage +
                            "',StartUp = '" + move.StartUp +
                            "',FrameAdvantage = '" + move.FrameAdvantage +
                            "',Active = '" + move.Active +
                            "',Recovery = '" + move.Recovery +
                            "',MoveType = '" + move.MoveType +
                            "' WHERE Characters.ID = '" + move.CharacterID + "' AND Command = '" + move.Command + "'";
                    }
                }
                else
                {
                    Console.WriteLine(character.Name + " Don't exist");
                    CheckGameID(character);
                    string query = "INSERT INTO Characters (Name,GameID,ImgUrl) VALUES ('" + character.Name + "','" + character.GameID + "','" + character.ImgUrl + "')";
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
                            if (CheckNoEmptyMove(move) == true)
                            {
                                move.CharacterID = character.ID;
                                query = "INSERT INTO Moves (CharacterID, Command, Guard, Damage, StartUp, FrameAdvantage, Active, Recovery, MoveType) VALUES ('"
                                    + move.CharacterID +
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
                        Console.WriteLine(character.Name + " Don't exist, fail to insert character");
                    }
                }
            }
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
