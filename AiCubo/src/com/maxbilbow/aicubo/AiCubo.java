
package com.maxbilbow.aicubo;

import click.rmx.engine.GameController;
public final class AiCubo extends GameController {	
	
	public static void main(String[] args) {
		try {
			GameController.getInstance().Start();
		} catch (Exception e) {
//			e.printStackTrace();
			System.exit(1);
		}
	}
}

	
