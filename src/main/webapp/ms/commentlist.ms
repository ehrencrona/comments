

<ul class="reviews-list">
	<li class="reviews-list-item clearfix comment-form-item">
		<div class="bubble">
			<div id="commentForm">
			<form action="submit"><textarea name="text" class="message"></textarea> 
				<input
				id="commentFormButton" type="button" value="Add comment" /></form>
			</div>
		</div>
	</li>

{{#comments}}
<li class="{{cssClass}}" id="{{id}}">

	{{> comment}}

</li>
{{/comments}}

</ul>

<li class="reply-list-item clearfix" id="replyForm">
	<div class="bubble">
		<form action="submit"><textarea name="text"></textarea> 
			<input id="replyFormReplyTo" type="hidden" value="" /> <input
			id="replyFormButton" type="button" value="Reply" />
		</form>
	</div>
</li>
