
package com.maxbilbow.aicubo;

import rmx.engine.GameController;
public final class AiCubo {	
	
	public static void main(String[] args) {
		try {
			GameController.getInstance().Start();
		} catch (Exception e) {
//			e.printStackTrace();
			System.exit(1);
		}
	}
}

	
