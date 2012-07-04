{{#hidden}}
	<div class="bubble collapsed"></div>
{{/hidden}}
{{^hidden}}

{{>poster}}

	<div class="bubble">
	
		<p class="message">{{text}} {{^full}}<a class="expand">{{^hasReplies}}More{{/hasReplies}}{{#hasReplies}}{{replyCount}}&nbsp;{{#hasMultipleReplies}}replies{{/hasMultipleReplies}}{{^hasMultipleReplies}}reply{{/hasMultipleReplies}} {{/hasReplies}}</a>{{/full}}</p>

		{{#full}}
		<span class="like">
			{{> like}} 
		</span>

		<span class="date">
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
