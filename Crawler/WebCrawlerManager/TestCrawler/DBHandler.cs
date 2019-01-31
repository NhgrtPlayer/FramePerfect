using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestCrawler
{
    class DBHandler
    {
        static public DBHandler InstanceDb;
        string ConnectionString = "Server=tcp:frameperfect.database.windows.net,1433;" +
            "Initial Catalog=FramePerfectData;" +
            "Persist Security Info=False;" +
            "User ID=perfectframer;" +
            "Password=FramePerfect2019;" +
            "MultipleActiveResultSets=False;" +
            "Encrypt=True;" +
            "TrustServerCertificate=False;" +
            "Connection Timeout=30;";
        private SqlConnection con;
        public DBHandler()
        {
            InstanceDb = this;
            con = new SqlConnection(ConnectionString);
        }

        public bool OpenConnection()
        {
            if (con == null)
                return false;
            con.Open();
            return true;
        }
        public bool CloseConnection()
        {
            if (con == null)
                return false;
            con.Close();
            return true;
        }

        public void ExectuteNoQuery(string query)
        {
            if (this.OpenConnection() == true)
            {
                SqlCommand cmd = new SqlCommand(query, con);
                int res = cmd.ExecuteNonQuery();
                Console.WriteLine(res + " lines affected with query:\n" + query);
                this.CloseConnection();
            }
            if (Program.debug == true)
                Console.WriteLine("End ExectuteNoQuery");
        }
        public List<string>[] Select(string query, int valueNb)
        {
            if (Program.debug == true)
                Console.WriteLine("Select");
            if (Program.debug == true)
                Console.WriteLine(query);
            List<string>[] list = new List<string>[valueNb];
            for (int i = 0; i < valueNb; i++)
                list[i] = new List<string>();
            //Create a list to store the resul

            if (this.OpenConnection() == true)
            {
                SqlCommand cmd = new SqlCommand(query, con);
                SqlDataReader dataReader = cmd.ExecuteReader();

                //Read the data and store them in the list
                while (dataReader.Read())
                    for (int i = 0; i < valueNb; i++)
                        list[i].Add(dataReader[i] + "");

                dataReader.Close();
                this.CloseConnection();
                return list;
            }
            else
            {
                return list;
            }
        }

    }
}
