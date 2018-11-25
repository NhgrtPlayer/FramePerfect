<!DOCTYPE html>
			<html>
			<head>
			<title>AIR Admin</title>
			<style>
				table {
					width:100%;
				}
				table, th, td {
					border: 1px solid black;
					border-collapse: collapse;
				}
				th, td {
					padding: 5px;
					text-align: left;
				}
				table#t01 tr:nth-child(even) {
					background-color: #eee;
				}
				table#t01 tr:nth-child(odd) {
				   background-color:#fff;
				}
				table#t01 th	{
					background-color: black;
					color: white;
				}
				</style>
				<meta charset="UTF-8">
			</head>
			<body><h3>AIR Administrative Console</h3><h3>Stores</h3><table id='t01' border=1><thead><tr><th>ID</th><th>Name</th><th>Description</th><th>URL</th><th>Longitude</th><th>Latitude</th><th>Updated</th><th>Deleted</th><th>Options</th></tr></thead><tbody><tr><td>1</td><td>Super Nova</td><td>Veliko blagdansko sniženje u odabranim dućanima u centru Super Nova.</td><td>http://cortex.foi.hr/mtl/courses/air/img/slika1.png</td><td>16307138</td><td>46319970</td><td>2017-10-22 14:15:42</td><td>2017-10-22 14:15:42</td><td><a href=discounts.php?storeId=1>Details</a>&nbsp;<a href=stores.php?id=1&action=0>Edit</a>&nbsp;<a href=stores.php?id=1&action=1>Delete</a></td></tr><tr><td>2</td><td>Varteks</td><td>Popust na sve kolekcije ljetne odjeće u Varteks Outlet dućanu.</td><td>http://cortex.foi.hr/mtl/courses/air/img/slika2.png</td><td>16343460</td><td>46292420</td><td>2018-10-30 12:20:57</td><td>2016-10-25 18:29:09</td><td><a href=discounts.php?storeId=2>Details</a>&nbsp;<a href=stores.php?id=2&action=0>Edit</a>&nbsp;<a href=stores.php?id=2&action=1>Delete</a></td></tr><tr><td>3</td><td>Prodaja Gudika</td><td>najjeftiniji odojci u regiji</td><td>https://en.wikipedia.org/wiki/Pig#/media/File:Sus_Barbatus,_the_Bornean_Bearded_Pig_(12616351323).jpg</td><td>16151514</td><td>49151514</td><td>2018-11-07 11:35:26</td><td>2018-11-07 11:35:26</td><td><a href=discounts.php?storeId=3>Details</a>&nbsp;<a href=stores.php?id=3&action=0>Edit</a>&nbsp;<a href=stores.php?id=3&action=1>Delete</a></td></tr></tbody></table><br/><form action='stores.php?id=0&action=2&save=0' method='post'> 
					<input type='submit' name='new' value='New Store' /></form>
			</body>
		</html>