package zyxhj.prize.repository;

import zyxhj.prize.domain.Prize;
import zyxhj.utils.data.rds.RDSRepository;

public class PrizeRepository extends RDSRepository<Prize>{
	public PrizeRepository() {
		super(Prize.class);
	}

}
