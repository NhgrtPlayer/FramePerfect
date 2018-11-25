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
			<body><h3>Detective Corp Headquarters</h3><a href="./characters.php">Back to Characters</a><br/><br/><br/><h3>Moves of selected character No.: 0</h3><table id='t01' border=1><thead><tr><th>ID</th><th>Name</th><th>Input</th><th>URL</th><th>Description</th><th>CHARACTER ID</th><th>Updated</th><th>Options</th></tr></thead><tbody><tr><td>1</td><td>Light Attack</td><td>5L</td><td>http://www.dustloop.com/wiki/images/thumb/5/54/DBFZ_AdultGohan_5L.png/175px-DBFZ_AdultGohan_5L.png</td><td>Description of Adult Gohan's 5L.</td><td>1</td><td>2015-10-31</td><td><a href=moves.php?id=4&storeId=0&action=0>Edit</a>&nbsp;<a href=moves.php?id=4&storeId=0&action=1>Delete</a></td></tr></tbody></table><br/><form action='moves.php?id=0&action=2&save=0&storeId=0' method='post'>
					<input type='submit' name='new' value='New Move' /></form>       
			</body>
		</html>