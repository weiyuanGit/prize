package zyxhj.prize.repository;

import zyxhj.prize.domain.WinningList;
import zyxhj.utils.data.rds.RDSRepository;

public class WinningListRepository extends RDSRepository<WinningList>{
	public WinningListRepository() {
		super(WinningList.class);
	}
}
