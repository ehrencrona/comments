
<h1>{{alias}}</h1>

<div>ID: {{id}}</div>

Favorites:

<ul>
{{#favorites}}
	<li>{{alias}}</li>
{{/favorites}}
</ul>

<form>
<input type="button" value="Add as favorite" id="favoriteFormButton"/>
</form>