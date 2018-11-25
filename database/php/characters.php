<!DOCTYPE html>
			<html>
			<head>
			<title>Frame Perfect - Admin</title>
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
				<title></title>
				<link href="css/style1.css" rel="stylesheet">
			</head>
			<body><h3>Detective Corp Headquarters</h3><a href="./games.php">Back to Games</a><br/><br/><br/><h3>Characters. Selected game No.: 0</h3><table id='t01' border=1><thead><tr><th>ID</th><th>Name</th><th>URL</th><th>Description</th><th>GAME ID</th><th>Updated</th><th>Options</th></tr></thead><tbody><tr><td>1</td><td>Adult Gohan</td><td>http://www.dustloop.com/wiki/images/thumb/0/08/DBFZ_Adult_Gohan_Portrait.png/235px-DBFZ_Adult_Gohan_Portrait.png</td><td>Description of Adult Gohan</td><td>1</td><td>2015-10-31</td><td><a href=characters.php?id=4&storeId=0&action=0>Edit</a>&nbsp;<a href=characters.php?id=4&storeId=0&action=1>Delete</a></td></tr></tbody></table><br/><form action='characters.php?id=0&action=2&save=0&storeId=0' method='post'>
					<input type='submit' name='new' value='New Character' /></form>       
			</body>
		</html>