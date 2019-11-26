package zyxhj.prize.repository;

import zyxhj.prize.domain.PrizeUser;
import zyxhj.utils.data.rds.RDSRepository;

public class PrizeUserRepository extends RDSRepository<PrizeUser>{
	public PrizeUserRepository() {
		super(PrizeUser.class);
	}
}
