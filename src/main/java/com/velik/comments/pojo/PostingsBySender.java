package com.velik.comments.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.iterator.PostingIdIterator;
import com.velik.comments.util.FetchingIterator;
import com.velik.comments.util.TransformingIterator;
import com.velik.comments.util.Tuple;

public class PostingsBySender<P extends Posting> implements Serializable {
	private static final long serialVersionUID = 1;

	private List<Tuple<PostingId, ProfileId>> postings = new ArrayList<Tuple<PostingId, ProfileId>>();
	private ProfileSetPojo posters;
	private Finder finder;

	public PostingsBySender(Finder finder) {
		this.finder = finder;
		this.posters = new ProfileSetPojo();
	}

	public Iterable<P> getPostings(final PostingList<P> exclude, int pageSize) {
		return new Iterable<P>() {

			@Override
			public Iterator<P> iterator() {
				final Iterator<PostingId> postingIdIterator = getPostingIdIterator();

				FetchingIterator<PostingId> filteredIterator = new FetchingIterator<PostingId>() {

					@Override
					protected PostingId fetch() {
						while (postingIdIterator.hasNext()) {
							PostingId next = postingIdIterator.next();

							if (!exclude.contains(next)) {
								return next;
							}
						}

						return null;
					}
				};

				return new PostingIdIterator<P>(filteredIterator, finder);
			}
		};
	}

	public Iterable<P> getPostings() {
		return new Iterable<P>() {

			@Override
			public Iterator<P> iterator() {
				Iterator<PostingId> postingIdIterator = getPostingIdIterator();

				return new PostingIdIterator<P>(postingIdIterator, finder);
			}
		};
	}

	protected Iterator<PostingId> getPostingIdIterator() {
		Iterator<PostingId> postingIdIterator = new TransformingIterator<Tuple<PostingId, ProfileId>, PostingId>(
				postings.iterator()) {

			@Override
			protected PostingId transform(Tuple<PostingId, ProfileId> next) {
				return next.getOne();
			}

		};
		return postingIdIterator;
	}

	public ProfileSet getFavoritesPosting(Profile profile) {
		return ((ProfileSetPojo) posters).intersection(profile.getFavoritesAsSet());
	}

	public PostingList<P> getPostingsInvolvingFavorites(Profile profile) {
		PostingListPojo<P> result = new PostingListPojo<P>(finder);

		for (Tuple<PostingId, ProfileId> posting : postings) {
			if (profile.getFavoritesAsSet().contains(posting.getTwo())) {
				result.add(posting.getOne());
			}
		}

		return result;
	}

	public void add(P posting) {
		postings.add(0, new Tuple<PostingId, ProfileId>(posting.getId(), posting.getPosterId()));
		posters.add(posting.getPosterId());
	}

	public boolean contains(PostingId id) {
		for (Tuple<PostingId, ProfileId> t : postings) {
			if (t.getOne().equals(id)) {
				return true;
			}
		}

		return false;
	}

	public ProfileSet getAllPosters() {
		return posters;
	}

	public int size() {
		return postings.size();
	}
}
