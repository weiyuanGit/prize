package zyxhj.prize.repository;

import zyxhj.prize.domain.FriendInvite;
import zyxhj.utils.data.rds.RDSRepository;

public class FriendInviteRepository extends RDSRepository<FriendInvite>{
	public FriendInviteRepository() {
		super(FriendInvite.class);
	}

}
