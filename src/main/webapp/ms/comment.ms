{{#hidden}}
	<div class="bubble collapsed"></div>
{{/hidden}}
{{^hidden}}

{{>poster}}

	<div class="bubble">
	
		<p class="message">{{text}} 
		
		{{^full}}
		<span class="date">
			<a href="#" class="expand">{{^hasReplies}}More{{/hasReplies}}{{#hasReplies}}{{replyCount}}&nbsp;{{#hasMultipleReplies}}replies{{/hasMultipleReplies}}{{^hasMultipleReplies}}reply{{/hasMultipleReplies}} {{/hasReplies}}</a>
		</span>
		{{/full}}
		</p>

		{{#full}}
		<span class="like">
			{{> like}} 
		</span>

		<span class="date" data-utc="2012-01-01T00:00Z">
			<a href="#" class="postButton">Reply</a>

			Yesterday, 12:00 
		</span>
		{{/full}}
	</div>

	{{#full}}
	<div class="clearfix"></div>

	<ul class="reply-list">

		{{#replies}}
			<li class="{{cssClass}}" id="{{id}}">
			{{> reply}}
			</li>
		{{/replies}}
	</ul>
	{{/full}}
{{/hidden}}	
