package zyxhj.cn.start;

import io.vertx.core.Vertx;
import zyxhj.utils.ZeroVerticle;

public class PrizeVerticle extends ZeroVerticle {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new PrizeVerticle());
	}

	public String name() {
		return "prize";
	}

	public int port() {
		return 8090;
	}
	protected void init() throws Exception {
//		initCtrl(ctrlMap, Singleton.ins(AppraiseService.class, "appraise"));
	}
}
