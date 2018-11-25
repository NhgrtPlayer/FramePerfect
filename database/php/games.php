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
			</head>
			<body><h3>Detective Corp Headquarters</h3><h3>Games</h3><table id='t01' border=1><thead><tr><th>ID</th><th>Name</th><th>Description</th><th>URL</th><th>Updated</th><th>Deleted</th><th>Options</th></tr></thead><tbody><tr><td>1</td><td>DRAGON BALL FighterZ</td><td>Description of DBFZ.</td><td>https://upload.wikimedia.org/wikipedia/fr/4/47/Dragon_Ball_FighterZ_Logo.png</td><td>2017-10-22 14:15:42</td><td><a href=characters.php?storeId=1>Details</a>&nbsp;<a href=games.php?id=1&action=0>Edit</a>&nbsp<a href=games.php?id=1&action=1>Delete</a></td></tr></tbody></table><br/><form action='games.php?id=0&action=2&save=0' method='post'> 
				<input type='submit' name='new' value='New Store' /></form>
			</body>
		</html>