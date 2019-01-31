using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FramePerfectCrawlerLibary

{
    public class Character
    {
        public string Name { get; set; }
        public string ID { get; set; }
        public List<Move> Moves { get; set; } = new List<Move>();
        public string GameID { get; set; }
        public string GameName { get; set; }
        public string ImgUrl { get; set; }
    }
}
