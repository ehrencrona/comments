

<div id="commentForm">
<form action="submit"><textarea name="text"></textarea> 
	<input
	id="postCommentButton" type="button" value="submit" /></form>
</div>

<ul class="commentList">

{{#comments}}
<li class="{{cssClass}}" id="{{id}}">

	{{> comment}}

</li>
{{/comments}}

</ul>

<div id="replyForm">
<form action="submit"><textarea name="text"></textarea> 
	<input id="replyFormReplyTo" type="hidden" value="" /> <input
	id="postReplyButton" type="button" value="submit" /></form>
</div>
