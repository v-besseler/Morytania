package com.arlania;

public final class Rasterizer extends DrawingArea {

	public static void clearCache() {
		shadowDecay = null;
		shadowDecay = null;
		SINE = null;
		COSINE = null;
		lineOffsets = null;
		hsl2rgb = null;
		depthBuffer = null;
		TextureLoader317.clear();
		TextureLoader667.clear();
	}
	
	public static void calculatePalette(double brightness, boolean pri) {
		brightness += Math.random() * 0.029999999999999999D - 0.014999999999999999D;
		int j = 0;
		for (int k = 0; k < 512; k++) {
			double d1 = (double) (k / 8) / 64D + 0.0078125D;
			double d2 = (double) (k & 7) / 8D + 0.0625D;
			for (int k1 = 0; k1 < 128; k1++) {
				double d3 = (double) k1 / 128D;
				double d4 = d3;
				double d5 = d3;
				double d6 = d3;
				if (d2 != 0.0D) {
					double d7;
					if (d3 < 0.5D)
						d7 = d3 * (1.0D + d2);
					else
						d7 = (d3 + d2) - d3 * d2;
					double d8 = 2D * d3 - d7;
					double d9 = d1 + 0.33333333333333331D;
					if (d9 > 1.0D)
						d9--;
					double d10 = d1;
					double d11 = d1 - 0.33333333333333331D;
					if (d11 < 0.0D)
						d11++;
					if (6D * d9 < 1.0D)
						d4 = d8 + (d7 - d8) * 6D * d9;
					else if (2D * d9 < 1.0D)
						d4 = d7;
					else if (3D * d9 < 2D)
						d4 = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
					else
						d4 = d8;
					if (6D * d10 < 1.0D)
						d5 = d8 + (d7 - d8) * 6D * d10;
					else if (2D * d10 < 1.0D)
						d5 = d7;
					else if (3D * d10 < 2D)
						d5 = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
					else
						d5 = d8;
					if (6D * d11 < 1.0D)
						d6 = d8 + (d7 - d8) * 6D * d11;
					else if (2D * d11 < 1.0D)
						d6 = d7;
					else if (3D * d11 < 2D)
						d6 = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
					else
						d6 = d8;
				}
				int l1 = (int) (d4 * 256D);
				int i2 = (int) (d5 * 256D);
				int j2 = (int) (d6 * 256D);
				int k2 = (l1 << 16) + (i2 << 8) + j2;
				k2 = adjustBrightness(k2, brightness);
				if (k2 == 0)
					k2 = 1;
				hsl2rgb[j++] = k2;
			}

		}
		if(pri) {
			TextureLoader317.calculateTexturePalette(brightness);
			TextureLoader667.calculateTexturePalette(brightness);
		}
	}

	public static int adjustBrightness(int i, double d) {
		double d1 = (double) (i >> 16) / 256D;
		double d2 = (double) (i >> 8 & 0xff) / 256D;
		double d3 = (double) (i & 0xff) / 256D;
		d1 = Math.pow(d1, d);
		d2 = Math.pow(d2, d);
		d3 = Math.pow(d3, d);
		int j = (int) (d1 * 256D);
		int k = (int) (d2 * 256D);
		int l = (int) (d3 * 256D);
		return (j << 16) + (k << 8) + l;
	}

	public static void setDefaultBounds() {
		lineOffsets = new int[DrawingArea.height];
		for (int j = 0; j < DrawingArea.height; j++)
			lineOffsets[j] = DrawingArea.width * j;

		center_x = DrawingArea.width / 2;
		center_y = DrawingArea.height / 2;
	}

	public static void setBounds(int j, int k) {
		lineOffsets = new int[k];
		for (int l = 0; l < k; l++)
			lineOffsets[l] = j * l;

		center_x = j / 2;
		center_y = k / 2;
	}


	public static void drawDepthShadedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int z1, int z2, int z3, int hsl1, int hsl2, 
			int hsl3)
	{
		if (!saveDepth) {
			drawShadedTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3);
			return;
		}
		int dx1 = 0;
		int dz1 = 0;
		int dhsl1 = 0;
		if(y2 != y1)
		{
			dx1 = (x2 - x1 << 16) / (y2 - y1);
			dz1 = (z2 - z1 << 16) / (y2 - y1);
			dhsl1 = (hsl2 - hsl1 << 15) / (y2 - y1);
		}
		int dx2 = 0;
		int dz2 = 0;
		int dhsl2 = 0;
		if(y3 != y2)
		{
			dx2 = (x3 - x2 << 16) / (y3 - y2);
			dz2 = (z3 - z2 << 16) / (y3 - y2);
			dhsl2 = (hsl3 - hsl2 << 15) / (y3 - y2);
		}
		int dx3 = 0;
		int dz3 = 0;
		int dhsl3 = 0;
		if(y3 != y1)
		{
			dx3 = (x1 - x3 << 16) / (y1 - y3);
			dz3 = (z1 - z3 << 16) / (y1 - y3);
			dhsl3 = (hsl1 - hsl3 << 15) / (y1 - y3);
		}
		if(y1 <= y2 && y1 <= y3)
		{
			if(y1 >= DrawingArea.bottomY)
				return;
			if(y2 > DrawingArea.bottomY)
				y2 = DrawingArea.bottomY;
			if(y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if(y2 < y3)
			{
				x3 = x1 <<= 16;
				z3 = z1 <<= 16;
				hsl3 = hsl1 <<= 15;
				if(y1 < 0)
				{
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					hsl3 -= dhsl3 * y1;
					hsl1 -= dhsl1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				z2 <<= 16;
				hsl2 <<= 15;
				if(y2 < 0)
				{
					x2 -= dx2 * y2;
					z2 -= dz2 * y2;
					hsl2 -= dhsl2 * y2;
					y2 = 0;
				}
				if(y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2)
				{
					y3 -= y2;
					y2 -= y1;
					for(y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width)
					{
						drawDepthShadedLine(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7);
						x3 += dx3;
						x1 += dx1;
						z3 += dz3;
						z1 += dz1;
						hsl3 += dhsl3;
						hsl1 += dhsl1;
					}

					while(--y3 >= 0) 
					{
						drawDepthShadedLine(DrawingArea.pixels, y1, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7);
						x3 += dx3;
						x2 += dx2;
						z3 += dz3;
						z2 += dz2;
						hsl3 += dhsl3;
						hsl2 += dhsl2;
						y1 += DrawingArea.width;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for(y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width)
				{
					drawDepthShadedLine(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7);
					x3 += dx3;
					x1 += dx1;
					z3 += dz3;
					z1 += dz1;
					hsl3 += dhsl3;
					hsl1 += dhsl1;
				}

				while(--y3 >= 0) 
				{
					drawDepthShadedLine(DrawingArea.pixels, y1, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7);
					x3 += dx3;
					x2 += dx2;
					z3 += dz3;
					z2 += dz2;
					hsl3 += dhsl3;
					hsl2 += dhsl2;
					y1 += DrawingArea.width;
				}
				return;
			}
			x2 = x1 <<= 16;
			z2 = z1 <<= 16;
			hsl2 = hsl1 <<= 15;
			if(y1 < 0)
			{
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				hsl2 -= dhsl3 * y1;
				hsl1 -= dhsl1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			z3 <<= 16;
			hsl3 <<= 15;
			if(y3 < 0)
			{
				x3 -= dx2 * y3;
				z3 -= dz2 * y3;
				hsl3 -= dhsl2 * y3;
				y3 = 0;
			}
			if(y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1)
			{
				y2 -= y3;
				y3 -= y1;
				for(y1 = lineOffsets[y1]; --y3 >= 0; y1 += DrawingArea.width)
				{
					drawDepthShadedLine(DrawingArea.pixels, y1, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7);
					x2 += dx3;
					x1 += dx1;
					z2 += dz3;
					z1 += dz1;
					hsl2 += dhsl3;
					hsl1 += dhsl1;
				}

				while(--y2 >= 0) 
				{
					drawDepthShadedLine(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7);
					x3 += dx2;
					x1 += dx1;
					z3 += dz2;
					z1 += dz1;
					hsl3 += dhsl2;
					hsl1 += dhsl1;
					y1 += DrawingArea.width;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for(y1 = lineOffsets[y1]; --y3 >= 0; y1 += DrawingArea.width)
			{
				drawDepthShadedLine(DrawingArea.pixels, y1, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7);
				x2 += dx3;
				x1 += dx1;
				z2 += dz3;
				z1 += dz1;
				hsl2 += dhsl3;
				hsl1 += dhsl1;
			}

			while(--y2 >= 0) 
			{
				drawDepthShadedLine(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7);
				x3 += dx2;
				x1 += dx1;
				z3 += dz2;
				z1 += dz1;
				hsl3 += dhsl2;
				hsl1 += dhsl1;
				y1 += DrawingArea.width;
			}
			return;
		}
		if(y2 <= y3)
		{
			if(y2 >= DrawingArea.bottomY)
				return;
			if(y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if(y1 > DrawingArea.bottomY)
				y1 = DrawingArea.bottomY;
			if(y3 < y1)
			{
				x1 = x2 <<= 16;
				z1 = z2 <<= 16;
				hsl1 = hsl2 <<= 15;
				if(y2 < 0)
				{
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					hsl1 -= dhsl1 * y2;
					hsl2 -= dhsl2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				z3 <<= 16;
				hsl3 <<= 15;
				if(y3 < 0)
				{
					x3 -= dx3 * y3;
					z3 -= dz3 * y3;
					hsl3 -= dhsl3 * y3;
					y3 = 0;
				}
				if(y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3)
				{
					y1 -= y3;
					y3 -= y2;
					for(y2 = lineOffsets[y2]; --y3 >= 0; y2 += DrawingArea.width)
					{
						drawDepthShadedLine(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7);
						x1 += dx1;
						x2 += dx2;
						z1 += dz1;
						z2 += dz2;
						hsl1 += dhsl1;
						hsl2 += dhsl2;
					}

					while(--y1 >= 0) 
					{
						drawDepthShadedLine(DrawingArea.pixels, y2, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7);
						x1 += dx1;
						x3 += dx3;
						z1 += dz1;
						z3 += dz3;
						hsl1 += dhsl1;
						hsl3 += dhsl3;
						y2 += DrawingArea.width;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for(y2 = lineOffsets[y2]; --y3 >= 0; y2 += DrawingArea.width)
				{
					drawDepthShadedLine(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7);
					x1 += dx1;
					x2 += dx2;
					z1 += dz1;
					z2 += dz2;
					hsl1 += dhsl1;
					hsl2 += dhsl2;
				}

				while(--y1 >= 0) 
				{
					drawDepthShadedLine(DrawingArea.pixels, y2, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7);
					x1 += dx1;
					x3 += dx3;
					z1 += dz1;
					z3 += dz3;
					hsl1 += dhsl1;
					hsl3 += dhsl3;
					y2 += DrawingArea.width;
				}
				return;
			}
			x3 = x2 <<= 16;
			z3 = z2 <<= 16;
			hsl3 = hsl2 <<= 15;
			if(y2 < 0)
			{
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				hsl3 -= dhsl1 * y2;
				hsl2 -= dhsl2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			z1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0)
			{
				x1 -= dx3 * y1;
				z1 -= dz3 * y1;
				hsl1 -= dhsl3 * y1;
				y1 = 0;
			}
			if(dx1 < dx2)
			{
				y3 -= y1;
				y1 -= y2;
				for(y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width)
				{
					drawDepthShadedLine(DrawingArea.pixels, y2, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7);
					x3 += dx1;
					x2 += dx2;
					z3 += dz1;
					z2 += dz2;
					hsl3 += dhsl1;
					hsl2 += dhsl2;
				}

				while(--y3 >= 0) 
				{
					drawDepthShadedLine(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7);
					x1 += dx3;
					x2 += dx2;
					z1 += dz3;
					z2 += dz2;
					hsl1 += dhsl3;
					hsl2 += dhsl2;
					y2 += DrawingArea.width;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for(y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width)
			{
				drawDepthShadedLine(DrawingArea.pixels, y2, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7);
				x3 += dx1;
				x2 += dx2;
				z3 += dz1;
				z2 += dz2;
				hsl3 += dhsl1;
				hsl2 += dhsl2;
			}

			while(--y3 >= 0) 
			{
				drawDepthShadedLine(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7);
				x1 += dx3;
				x2 += dx2;
				z1 += dz3;
				z2 += dz2;
				hsl1 += dhsl3;
				hsl2 += dhsl2;
				y2 += DrawingArea.width;
			}
			return;
		}
		if(y3 >= DrawingArea.bottomY)
			return;
		if(y1 > DrawingArea.bottomY)
			y1 = DrawingArea.bottomY;
		if(y2 > DrawingArea.bottomY)
			y2 = DrawingArea.bottomY;
		if(y1 < y2)
		{
			x2 = x3 <<= 16;
			z2 = z3 <<= 16;
			hsl2 = hsl3 <<= 15;
			if(y3 < 0)
			{
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				hsl2 -= dhsl2 * y3;
				hsl3 -= dhsl3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			z1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0)
			{
				x1 -= dx1 * y1;
				z1 -= dz1 * y1;
				hsl1 -= dhsl1 * y1;
				y1 = 0;
			}
			if(dx2 < dx3)
			{
				y2 -= y1;
				y1 -= y3;
				for(y3 = lineOffsets[y3]; --y1 >= 0; y3 += DrawingArea.width)
				{
					drawDepthShadedLine(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7);
					x2 += dx2;
					x3 += dx3;
					z2 += dz2;
					z3 += dz3;
					hsl2 += dhsl2;
					hsl3 += dhsl3;
				}

				while(--y2 >= 0) 
				{
					drawDepthShadedLine(DrawingArea.pixels, y3, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7);
					x2 += dx2;
					x1 += dx1;
					z2 += dz2;
					z1 += dz1;
					hsl2 += dhsl2;
					hsl1 += dhsl1;
					y3 += DrawingArea.width;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for(y3 = lineOffsets[y3]; --y1 >= 0; y3 += DrawingArea.width)
			{
				drawDepthShadedLine(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7);
				x2 += dx2;
				x3 += dx3;
				z2 += dz2;
				z3 += dz3;
				hsl2 += dhsl2;
				hsl3 += dhsl3;
			}

			while(--y2 >= 0) 
			{
				drawDepthShadedLine(DrawingArea.pixels, y3, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7);
				x2 += dx2;
				x1 += dx1;
				z2 += dz2;
				z1 += dz1;
				hsl2 += dhsl2;
				hsl1 += dhsl1;
				y3 += DrawingArea.width;
			}
			return;
		}
		x1 = x3 <<= 16;
		z1 = z3 <<= 16;
		hsl1 = hsl3 <<= 15;
		if(y3 < 0)
		{
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			hsl1 -= dhsl2 * y3;
			hsl3 -= dhsl3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		z2 <<= 16;
		hsl2 <<= 15;
		if(y2 < 0)
		{
			x2 -= dx1 * y2;
			z2 -= dz1 * y2;
			hsl2 -= dhsl1 * y2;
			y2 = 0;
		}
		if(dx2 < dx3)
		{
			y1 -= y2;
			y2 -= y3;
			for(y3 = lineOffsets[y3]; --y2 >= 0; y3 += DrawingArea.width)
			{
				drawDepthShadedLine(DrawingArea.pixels, y3, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7);
				x1 += dx2;
				x3 += dx3;
				z1 += dz2;
				z3 += dz3;
				hsl1 += dhsl2;
				hsl3 += dhsl3;
			}

			while(--y1 >= 0) 
			{
				drawDepthShadedLine(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7);
				x2 += dx1;
				x3 += dx3;
				z2 += dz1;
				z3 += dz3;
				hsl2 += dhsl1;
				hsl3 += dhsl3;
				y3 += DrawingArea.width;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for(y3 = lineOffsets[y3]; --y2 >= 0; y3 += DrawingArea.width)
		{
			drawDepthShadedLine(DrawingArea.pixels, y3, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7);
			x1 += dx2;
			x3 += dx3;
			z1 += dz2;
			z3 += dz3;
			hsl1 += dhsl2;
			hsl3 += dhsl3;
		}

		while(--y1 >= 0) 
		{
			drawDepthShadedLine(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7);
			x2 += dx1;
			x3 += dx3;
			z2 += dz1;
			z3 += dz3;
			hsl2 += dhsl1;
			hsl3 += dhsl3;
			y3 += DrawingArea.width;
		}
	}

	private static void drawDepthShadedLine(int[] dst, int off, int x1, int x2, int z1, int z2, int hsl1, int hsl2) {
		int rgb;// was parameter
		int len;// was parameter
		if (notTextured) {
			int dz = 0;
			if (x2 - x1 > 0) {
				dz = (z2 - z1) / (x2 - x1);
			}
			int dhsl;
			if (restrict_edges) {
				if (x2 - x1 > 3)
					dhsl = (hsl2 - hsl1) / (x2 - x1);
				else
					dhsl = 0;
				if (x2 > DrawingArea.viewportRX)
					x2 = DrawingArea.viewportRX;
				if (x1 < 0) {
					z1 -= x1 * dz;
					hsl1 -= x1 * dhsl;
					x1 = 0;
				}
				if (x1 >= x2)
					return;
				off += x1;
				len = x2 - x1 >> 2;
				dhsl <<= 2;
			} else {
				if (x1 >= x2)
					return;
				off += x1;
				len = x2 - x1 >> 2;
				if (len > 0)
					dhsl = (hsl2 - hsl1) * shadowDecay[len] >> 15;
					else
						dhsl = 0;
			}
			if (alpha == 0) {
				while (--len >= 0) {
					rgb = hsl2rgb[hsl1 >> 8];
					hsl1 += dhsl;
					depthBuffer[off] = z1;
					dst[off++] = rgb;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = rgb;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = rgb;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = rgb;
					z1 += dz;
				}
				len = x2 - x1 & 3;
				if (len > 0) {
					rgb = hsl2rgb[hsl1 >> 8];
					do {
						depthBuffer[off] = z1;
						dst[off++] = rgb;
						z1 += dz;
					} while (--len > 0);
					return;
				}
			} else {
				int j2 = alpha;
				int l2 = 256 - alpha;
				while (--len >= 0) {
					rgb = hsl2rgb[hsl1 >> 8];
					hsl1 += dhsl;
					rgb = ((rgb & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * l2 >> 8 & 0xff00);
					dst[off] = rgb + ((dst[off] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j2 >> 8 & 0xff00);
					depthBuffer[off] = z1 * l2 + depthBuffer[off] * j2 >> 8;
					off++;
					z1 += dz;
					dst[off] = rgb + ((dst[off] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j2 >> 8 & 0xff00);
					depthBuffer[off] = z1 * l2 + depthBuffer[off] * j2 >> 8;
					off++;
					z1 += dz;
					dst[off] = rgb + ((dst[off] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j2 >> 8 & 0xff00);
					depthBuffer[off] = z1 * l2 + depthBuffer[off] * j2 >> 8;
					off++;
					z1 += dz;
					dst[off] = rgb + ((dst[off] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j2 >> 8 & 0xff00);
					depthBuffer[off] = z1 * l2 + depthBuffer[off] * j2 >> 8;
					off++;
					z1 += dz;
				}
				len = x2 - x1 & 3;// dugi
				if (len > 0) {
					rgb = hsl2rgb[hsl1 >> 8];
					rgb = ((rgb & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * l2 >> 8 & 0xff00);
					do {
						dst[off] = rgb + ((dst[off] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j2 >> 8 & 0xff00);
						depthBuffer[off] = z1 * l2 + depthBuffer[off] * j2 >> 8;
						off++;
						z1 += dz;
					} while (--len > 0);
				}
			}
			return;
		}
		if (x1 >= x2)
			return;
		int dz = (z2 - z1) / (x2 - x1);
		int dhsl = (hsl2 - hsl1) / (x2 - x1);
		if (restrict_edges) {
			if (x2 > DrawingArea.viewportRX)
				x2 = DrawingArea.viewportRX;
			if (x1 < 0) {
				z1 -= x1 * dz;
				hsl1 -= x1 * dhsl;
				x1 = 0;
			}
			if (x1 >= x2)
				return;
		}
		off += x1;
		len = x2 - x1;
		if (alpha == 0) {
			do {
				depthBuffer[off] = z1;
				dst[off++] = hsl2rgb[hsl1 >> 8];
				hsl1 += dhsl;
				z1 += dz;
			} while (--len > 0);
			return;
		}
		int k2 = alpha;
		int i3 = 256 - alpha;
		do {
			rgb = hsl2rgb[hsl1 >> 8];
			hsl1 += dhsl;
			rgb = ((rgb & 0xff00ff) * i3 >> 8 & 0xff00ff) + ((rgb & 0xff00) * i3 >> 8 & 0xff00);
			dst[off] = rgb + ((dst[off] & 0xff00ff) * k2 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * k2 >> 8 & 0xff00);
			depthBuffer[off] = z1 * i3 + depthBuffer[off] * k2 >> 8;
			z1 += dz;
			off++;
		} while (--len > 0);
	}

	public static void drawDepthFlatTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int z1, int z2, int z3, int rgb) {
		if (!saveDepth) {
			drawFlatTriangle(y1, y2, y3, x1, x2, x3, rgb);
			return;
		}
		int dx1 = 0;
		int dz1 = 0;
		if (y2 != y1) {
			dx1 = (x2 - x1 << 16) / (y2 - y1);
			dz1 = (z2 - z1 << 16) / (y2 - y1);
		}
		int dx2 = 0;
		int dz2 = 0;
		if (y3 != y2) {
			dx2 = (x3 - x2 << 16) / (y3 - y2);
			dz2 = (z3 - z2 << 16) / (y3 - y2);
		}
		int dx3 = 0;
		int dz3 = 0;
		if (y3 != y1) {
			dx3 = (x1 - x3 << 16) / (y1 - y3);
			dz3 = (z1 - z3 << 16) / (y1 - y3);
		}
		if (y1 <= y2 && y1 <= y3) {
			if (y1 >= DrawingArea.bottomY)
				return;
			if (y2 > DrawingArea.bottomY)
				y2 = DrawingArea.bottomY;
			if (y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if (y2 < y3) {
				x3 = x1 <<= 16;
				z3 = z1 <<= 16;
				if (y1 < 0) {
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				z2 <<= 16;
				if (y2 < 0) {
					x2 -= dx2 * y2;
					z2 -= dz2 * y2;
					y2 = 0;
				}
				if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
					y3 -= y2;
					y2 -= y1;
					for (y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width) {
						drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x3 >> 16, x1 >> 16, z3, z1);
						x3 += dx3;
						x1 += dx1;
						z3 += dz3;
						z1 += dz1;
					}

					while (--y3 >= 0) {
						drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x3 >> 16, x2 >> 16, z3, z2);
						x3 += dx3;
						x2 += dx2;
						z3 += dz3;
						z2 += dz2;
						y1 += DrawingArea.width;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for (y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width) {
					drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, z3);
					x3 += dx3;
					x1 += dx1;
					z3 += dz3;
					z1 += dz1;
				}

				while (--y3 >= 0) {
					drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x2 >> 16, x3 >> 16, z2, z3);
					x3 += dx3;
					x2 += dx2;
					z3 += dz3;
					z2 += dz2;
					y1 += DrawingArea.width;
				}
				return;
			}
			x2 = x1 <<= 16;
			z2 = z1 <<= 16;
			if (y1 < 0) {
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			z3 <<= 16;
			if (y3 < 0) {
				x3 -= dx2 * y3;
				z3 -= dz2 * y3;
				y3 = 0;
			}
			if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
				y2 -= y3;
				y3 -= y1;
				for (y1 = lineOffsets[y1]; --y3 >= 0; y1 += DrawingArea.width) {
					drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x2 >> 16, x1 >> 16, z2, z1);
					x2 += dx3;
					x1 += dx1;
					z2 += dz3;
					z1 += dz1;
				}

				while (--y2 >= 0) {
					drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x3 >> 16, x1 >> 16, z3, z1);
					x3 += dx2;
					x1 += dx1;
					z3 += dz2;
					z1 += dz1;
					y1 += DrawingArea.width;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for (y1 = lineOffsets[y1]; --y3 >= 0; y1 += DrawingArea.width) {
				drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x1 >> 16, x2 >> 16, z1, z2);
				x2 += dx3;
				x1 += dx1;
				z2 += dz3;
				z1 += dz1;
			}

			while (--y2 >= 0) {
				drawDepthFlatLine(DrawingArea.pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, z3);
				x3 += dx2;
				x1 += dx1;
				z3 += dz2;
				z1 += dz1;
				y1 += DrawingArea.width;
			}
			return;
		}
		if (y2 <= y3) {
			if (y2 >= DrawingArea.bottomY)
				return;
			if (y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if (y1 > DrawingArea.bottomY)
				y1 = DrawingArea.bottomY;
			if (y3 < y1) {
				x1 = x2 <<= 16;
				z1 = z2 <<= 16;
				if (y2 < 0) {
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				z3 <<= 16;
				if (y3 < 0) {
					x3 -= dx3 * y3;
					z3 -= dz3 * y3;
					y3 = 0;
				}
				if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
					y1 -= y3;
					y3 -= y2;
					for (y2 = lineOffsets[y2]; --y3 >= 0; y2 += DrawingArea.width) {
						drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x1 >> 16, x2 >> 16, z1, z2);
						x1 += dx1;
						x2 += dx2;
						z1 += dz1;
						z2 += dz2;
					}

					while (--y1 >= 0) {
						drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x1 >> 16, x3 >> 16, z1, z3);
						x1 += dx1;
						x3 += dx3;
						z1 += dz1;
						z3 += dz3;
						y2 += DrawingArea.width;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for (y2 = lineOffsets[y2]; --y3 >= 0; y2 += DrawingArea.width) {
					drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, z1);
					x1 += dx1;
					x2 += dx2;
					z1 += dz1;
					z2 += dz2;
				}

				while (--y1 >= 0) {
					drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x3 >> 16, x1 >> 16, z3, z1);
					x1 += dx1;
					x3 += dx3;
					z1 += dz1;
					z3 += dz3;
					y2 += DrawingArea.width;
				}
				return;
			}
			x3 = x2 <<= 16;
			z3 = z2 <<= 16;
			if (y2 < 0) {
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			z1 <<= 16;
			if (y1 < 0) {
				x1 -= dx3 * y1;
				z1 -= dz3 * y1;
				y1 = 0;
			}
			if (dx1 < dx2) {
				y3 -= y1;
				y1 -= y2;
				for (y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width) {
					drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x3 >> 16, x2 >> 16, z3, z2);
					x3 += dx1;
					x2 += dx2;
					z3 += dz1;
					z2 += dz2;
				}

				while (--y3 >= 0) {
					drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x1 >> 16, x2 >> 16, z1, z2);
					x1 += dx3;
					x2 += dx2;
					z1 += dz3;
					z2 += dz2;
					y2 += DrawingArea.width;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for (y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width) {
				drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x2 >> 16, x3 >> 16, z2, z3);
				x3 += dx1;
				x2 += dx2;
				z3 += dz1;
				z2 += dz2;
			}

			while (--y3 >= 0) {
				drawDepthFlatLine(DrawingArea.pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, z1);
				x1 += dx3;
				x2 += dx2;
				z1 += dz3;
				z2 += dz2;
				y2 += DrawingArea.width;
			}
			return;
		}
		if (y3 >= DrawingArea.bottomY)
			return;
		if (y1 > DrawingArea.bottomY)
			y1 = DrawingArea.bottomY;
		if (y2 > DrawingArea.bottomY)
			y2 = DrawingArea.bottomY;
		if (y1 < y2) {
			x2 = x3 <<= 16;
			z2 = z3 <<= 16;
			if (y3 < 0) {
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			z1 <<= 16;
			if (y1 < 0) {
				x1 -= dx1 * y1;
				z1 -= dz1 * y1;
				y1 = 0;
			}
			if (dx2 < dx3) {
				y2 -= y1;
				y1 -= y3;
				for (y3 = lineOffsets[y3]; --y1 >= 0; y3 += DrawingArea.width) {
					drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x2 >> 16, x3 >> 16, z2, z3);
					x2 += dx2;
					x3 += dx3;
					z2 += dz2;
					z3 += dz3;
				}

				while (--y2 >= 0) {
					drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x2 >> 16, x1 >> 16, z2, z1);
					x2 += dx2;
					x1 += dx1;
					z2 += dz2;
					z1 += dz1;
					y3 += DrawingArea.width;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for (y3 = lineOffsets[y3]; --y1 >= 0; y3 += DrawingArea.width) {
				drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, z2);
				x2 += dx2;
				x3 += dx3;
				z2 += dz2;
				z3 += dz3;
			}

			while (--y2 >= 0) {
				drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x1 >> 16, x2 >> 16, z1, z2);
				x2 += dx2;
				x1 += dx1;
				z2 += dz2;
				z1 += dz1;
				y3 += DrawingArea.width;
			}
			return;
		}
		x1 = x3 <<= 16;
		z1 = z3 <<= 16;
		if (y3 < 0) {
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		z2 <<= 16;
		if (y2 < 0) {
			x2 -= dx1 * y2;
			z2 -= dz1 * y2;
			y2 = 0;
		}
		if (dx2 < dx3) {
			y1 -= y2;
			y2 -= y3;
			for (y3 = lineOffsets[y3]; --y2 >= 0; y3 += DrawingArea.width) {
				drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x1 >> 16, x3 >> 16, z1, z3);
				x1 += dx2;
				x3 += dx3;
				z1 += dz2;
				z3 += dz3;
			}

			while (--y1 >= 0) {
				drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x2 >> 16, x3 >> 16, z2, z3);
				x2 += dx1;
				x3 += dx3;
				z2 += dz1;
				z3 += dz3;
				y3 += DrawingArea.width;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for (y3 = lineOffsets[y3]; --y2 >= 0; y3 += DrawingArea.width) {
			drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x3 >> 16, x1 >> 16, z3, z1);
			x1 += dx2;
			x3 += dx3;
			z1 += dz2;
			z3 += dz3;
		}

		while (--y1 >= 0) {
			drawDepthFlatLine(DrawingArea.pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, z2);
			x2 += dx1;
			x3 += dx3;
			z2 += dz1;
			z3 += dz3;
			y3 += DrawingArea.width;
		}
	}

	private static void drawDepthFlatLine(int[] dst, int off, int rgb, int x1, int x2, int z1, int z2) {
		int len;// was parameter
		int dz = 0;
		if (x1 != x2) {
			dz = (z2 - z1) / (x2 - x1);
		}
		if (restrict_edges) {
			if (x2 > DrawingArea.viewportRX)
				x2 = DrawingArea.viewportRX;
			if (x1 < 0) {
				z1 -= x1 * dz;
				x1 = 0;
			}
		}
		if (x1 >= x2)
			return;
		off += x1;
		len = x2 - x1 >> 2;
		if (alpha == 0) {
			while (--len >= 0) {
				depthBuffer[off] = z1;
				dst[off++] = rgb;
				z1 += dz;
				depthBuffer[off] = z1;
				dst[off++] = rgb;
				z1 += dz;
				depthBuffer[off] = z1;
				dst[off++] = rgb;
				z1 += dz;
				depthBuffer[off] = z1;
				dst[off++] = rgb;
				z1 += dz;
			}
			for (len = x2 - x1 & 3; --len >= 0;) {
				depthBuffer[off] = z1;
				dst[off++] = rgb;
				z1 += dz;
			}

			return;
		}
		int j1 = alpha;
		int k1 = 256 - alpha;
		rgb = ((rgb & 0xff00ff) * k1 >> 8 & 0xff00ff) + ((rgb & 0xff00) * k1 >> 8 & 0xff00);
		while (--len >= 0) {
			depthBuffer[off] = z1 * k1 + depthBuffer[off] * j1 >> 8;
			dst[off++] = rgb + ((dst[off] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j1 >> 8 & 0xff00);
			z1 += dz;
			depthBuffer[off] = z1 * k1 + depthBuffer[off] * j1 >> 8;
			dst[off++] = rgb + ((dst[off] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j1 >> 8 & 0xff00);
			z1 += dz;
			depthBuffer[off] = z1 * k1 + depthBuffer[off] * j1 >> 8;
			dst[off++] = rgb + ((dst[off] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j1 >> 8 & 0xff00);
			z1 += dz;
			depthBuffer[off] = z1 * k1 + depthBuffer[off] * j1 >> 8;
			dst[off++] = rgb + ((dst[off] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j1 >> 8 & 0xff00);
			z1 += dz;
		}
		for (len = x2 - x1 & 3; --len >= 0;) {
			depthBuffer[off] = z1 * k1 + depthBuffer[off] * j1 >> 8;
			dst[off++] = rgb + ((dst[off] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * j1 >> 8 & 0xff00);
			z1 += dz;
		}
	}

	public static void drawDepthTexturedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int z1, int z2, int z3, int b1, int b2, int b3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int tex) {
		brighten = tex == 1;
		//System.out.println("brighten ="+brighten);
		if (!saveDepth) {
			drawTexturedTriangle(y1, y2, y3, x1, x2, x3, b1, b2, b3, tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3, tex);
			return;
		}
		int ai[] = null;
		if(Client.getOption("hd_tex")) {
			ai = TextureLoader667.getTexturePixels(tex);
		} else {
			ai = TextureLoader317.getTexturePixels(tex);
		}
		opaque = false;
		if(!Client.getOption("hd_tex"))
			opaque = !TextureLoader317.textureIsTransparent[tex];
		tx2 = tx1 - tx2;
		ty2 = ty1 - ty2;
		tz2 = tz1 - tz2;
		tx3 -= tx1;
		ty3 -= ty1;
		tz3 -= tz1;
		int l4 = (tx3 * ty1 - ty3 * tx1) * WorldController.focalLength << 5;
		int i5 = ty3 * tz1 - tz3 * ty1 << 8;
		int j5 = tz3 * tx1 - tx3 * tz1 << 5;
		int k5 = (tx2 * ty1 - ty2 * tx1) * WorldController.focalLength << 5;
		int l5 = ty2 * tz1 - tz2 * ty1 << 8;
		int i6 = tz2 * tx1 - tx2 * tz1 << 5;
		int j6 = (ty2 * tx3 - tx2 * ty3) * WorldController.focalLength << 5;
		int k6 = tz2 * ty3 - ty2 * tz3 << 8;
		int l6 = tx2 * tz3 - tz2 * tx3 << 5;
		int dx1 = 0;
		int dz1 = 0;
		int db1 = 0;
		if (y2 != y1) {
			dx1 = (x2 - x1 << 16) / (y2 - y1);
			dz1 = (z2 - z1 << 16) / (y2 - y1);
			db1 = (b2 - b1 << 16) / (y2 - y1);
		}
		int dx2 = 0;
		int dz2 = 0;
		int db2 = 0;
		if (y3 != y2) {
			dx2 = (x3 - x2 << 16) / (y3 - y2);
			dz2 = (z3 - z2 << 16) / (y3 - y2);
			db2 = (b3 - b2 << 16) / (y3 - y2);
		}
		int dx3 = 0;
		int dz3 = 0;
		int db3 = 0;
		if (y3 != y1) {
			dx3 = (x1 - x3 << 16) / (y1 - y3);
			dz3 = (z1 - z3 << 16) / (y1 - y3);
			db3 = (b1 - b3 << 16) / (y1 - y3);
		}
		if (y1 <= y2 && y1 <= y3) {
			if (y1 >= DrawingArea.bottomY)
				return;
			if (y2 > DrawingArea.bottomY)
				y2 = DrawingArea.bottomY;
			if (y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if (y2 < y3) {
				x3 = x1 <<= 16;
				z3 = z1 <<= 16;
				b3 = b1 <<= 16;
				if (y1 < 0) {
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					b3 -= db3 * y1;
					b1 -= db1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				z2 <<= 16;
				b2 <<= 16;
				if (y2 < 0) {
					x2 -= dx2 * y2;
					z2 -= dz2 * y2;
					b2 -= db2 * y2;
					y2 = 0;
				}
				int k8 = y1 - center_y;
				l4 += j5 * k8;
				k5 += i6 * k8;
				j6 += l6 * k8;
				if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
					y3 -= y2;
					y2 -= y1;
					y1 = lineOffsets[y1];
					while (--y2 >= 0) {
						drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x3 >> 16, x1 >> 16, z3, z1, b3 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
						x3 += dx3;
						x1 += dx1;
						z3 += dz3;
						z1 += dz1;
						b3 += db3;
						b1 += db1;
						y1 += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--y3 >= 0) {
						drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x3 >> 16, x2 >> 16, z3, z2, b3 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
						x3 += dx3;
						x2 += dx2;
						z3 += dz3;
						z2 += dz2;
						b3 += db3;
						b2 += db2;
						y1 += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				y1 = lineOffsets[y1];
				while (--y2 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x1 >> 16, x3 >> 16, z1, z3, b1 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += dx3;
					x1 += dx1;
					z3 += dz3;
					z1 += dz1;
					b3 += db3;
					b1 += db1;
					y1 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y3 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x2 >> 16, x3 >> 16, z2, z3, b2 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += dx3;
					x2 += dx2;
					z3 += dz3;
					z2 += dz2;
					b3 += db3;
					b2 += db2;
					y1 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x2 = x1 <<= 16;
			z2 = z1 <<= 16;
			b2 = b1 <<= 16;
			if (y1 < 0) {
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				b2 -= db3 * y1;
				b1 -= db1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			z3 <<= 16;
			b3 <<= 16;
			if (y3 < 0) {
				x3 -= dx2 * y3;
				z3 -= dz2 * y3;
				b3 -= db2 * y3;
				y3 = 0;
			}
			int l8 = y1 - center_y;
			l4 += j5 * l8;
			k5 += i6 * l8;
			j6 += l6 * l8;
			if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
				y2 -= y3;
				y3 -= y1;
				y1 = lineOffsets[y1];
				while (--y3 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x2 >> 16, x1 >> 16, z2, z1, b2 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
					x2 += dx3;
					x1 += dx1;
					z2 += dz3;
					z1 += dz1;
					b2 += db3;
					b1 += db1;
					y1 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y2 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x3 >> 16, x1 >> 16, z3, z1, b3 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += dx2;
					x1 += dx1;
					z3 += dz2;
					z1 += dz1;
					b3 += db2;
					b1 += db1;
					y1 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			y1 = lineOffsets[y1];
			while (--y3 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x1 >> 16, x2 >> 16, z1, z2, b1 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += dx3;
				x1 += dx1;
				z2 += dz3;
				z1 += dz1;
				b2 += db3;
				b1 += db1;
				y1 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y2 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y1, x1 >> 16, x3 >> 16, z1, z3, b1 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
				x3 += dx2;
				x1 += dx1;
				z3 += dz2;
				z1 += dz1;
				b3 += db2;
				b1 += db1;
				y1 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (y2 <= y3) {
			if (y2 >= DrawingArea.bottomY)
				return;
			if (y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if (y1 > DrawingArea.bottomY)
				y1 = DrawingArea.bottomY;
			if (y3 < y1) {
				x1 = x2 <<= 16;
				z1 = z2 <<= 16;
				b1 = b2 <<= 16;
				if (y2 < 0) {
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					b1 -= db1 * y2;
					b2 -= db2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				z3 <<= 16;
				b3 <<= 16;
				if (y3 < 0) {
					x3 -= dx3 * y3;
					z3 -= dz3 * y3;
					b3 -= db3 * y3;
					y3 = 0;
				}
				int i9 = y2 - center_y;
				l4 += j5 * i9;
				k5 += i6 * i9;
				j6 += l6 * i9;
				if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
					y1 -= y3;
					y3 -= y2;
					y2 = lineOffsets[y2];
					while (--y3 >= 0) {
						drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x1 >> 16, x2 >> 16, z1, z2, b1 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
						x1 += dx1;
						x2 += dx2;
						z1 += dz1;
						z2 += dz2;
						b1 += db1;
						b2 += db2;
						y2 += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--y1 >= 0) {
						drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x1 >> 16, x3 >> 16, z1, z3, b1 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
						x1 += dx1;
						x3 += dx3;
						z1 += dz1;
						z3 += dz3;
						b1 += db1;
						b3 += db3;
						y2 += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				y2 = lineOffsets[y2];
				while (--y3 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x2 >> 16, x1 >> 16, z2, z1, b2 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
					x1 += dx1;
					x2 += dx2;
					z1 += dz1;
					z2 += dz2;
					b1 += db1;
					b2 += db2;
					y2 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y1 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x3 >> 16, x1 >> 16, z3, z1, b3 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
					x1 += dx1;
					x3 += dx3;
					z1 += dz1;
					z3 += dz3;
					b1 += db1;
					b3 += db3;
					y2 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x3 = x2 <<= 16;
			z3 = z2 <<= 16;
			b3 = b2 <<= 16;
			if (y2 < 0) {
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				b3 -= db1 * y2;
				b2 -= db2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			z1 <<= 16;
			b1 <<= 16;
			if (y1 < 0) {
				x1 -= dx3 * y1;
				z1 -= dz3 * y1;
				b1 -= db3 * y1;
				y1 = 0;
			}
			int j9 = y2 - center_y;
			l4 += j5 * j9;
			k5 += i6 * j9;
			j6 += l6 * j9;
			if (dx1 < dx2) {
				y3 -= y1;
				y1 -= y2;
				y2 = lineOffsets[y2];
				while (--y1 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x3 >> 16, x2 >> 16, z3, z2, b3 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += dx1;
					x2 += dx2;
					z3 += dz1;
					z2 += dz2;
					b3 += db1;
					b2 += db2;
					y2 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y3 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x1 >> 16, x2 >> 16, z1, z2, b1 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
					x1 += dx3;
					x2 += dx2;
					z1 += dz3;
					z2 += dz2;
					b1 += db3;
					b2 += db2;
					y2 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			y2 = lineOffsets[y2];
			while (--y1 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x2 >> 16, x3 >> 16, z2, z3, b2 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
				x3 += dx1;
				x2 += dx2;
				z3 += dz1;
				z2 += dz2;
				b3 += db1;
				b2 += db2;
				y2 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y3 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y2, x2 >> 16, x1 >> 16, z2, z1, b2 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
				x1 += dx3;
				x2 += dx2;
				z1 += dz3;
				z2 += dz2;
				b1 += db3;
				b2 += db2;
				y2 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (y3 >= DrawingArea.bottomY)
			return;
		if (y1 > DrawingArea.bottomY)
			y1 = DrawingArea.bottomY;
		if (y2 > DrawingArea.bottomY)
			y2 = DrawingArea.bottomY;
		if (y1 < y2) {
			x2 = x3 <<= 16;
			z2 = z3 <<= 16;
			b2 = b3 <<= 16;
			if (y3 < 0) {
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				b2 -= db2 * y3;
				b3 -= db3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			z1 <<= 16;
			b1 <<= 16;
			if (y1 < 0) {
				x1 -= dx1 * y1;
				z1 -= dz1 * y1;
				b1 -= db1 * y1;
				y1 = 0;
			}
			int k9 = y3 - center_y;
			l4 += j5 * k9;
			k5 += i6 * k9;
			j6 += l6 * k9;
			if (dx2 < dx3) {
				y2 -= y1;
				y1 -= y3;
				y3 = lineOffsets[y3];
				while (--y1 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x2 >> 16, x3 >> 16, z2, z3, b2 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
					x2 += dx2;
					x3 += dx3;
					z2 += dz2;
					z3 += dz3;
					b2 += db2;
					b3 += db3;
					y3 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y2 >= 0) {
					drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x2 >> 16, x1 >> 16, z2, z1, b2 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
					x2 += dx2;
					x1 += dx1;
					z2 += dz2;
					z1 += dz1;
					b2 += db2;
					b1 += db1;
					y3 += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			y3 = lineOffsets[y3];
			while (--y1 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x3 >> 16, x2 >> 16, z3, z2, b3 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += dx2;
				x3 += dx3;
				z2 += dz2;
				z3 += dz3;
				b2 += db2;
				b3 += db3;
				y3 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y2 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x1 >> 16, x2 >> 16, z1, z2, b1 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += dx2;
				x1 += dx1;
				z2 += dz2;
				z1 += dz1;
				b2 += db2;
				b1 += db1;
				y3 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		x1 = x3 <<= 16;
		z1 = z3 <<= 16;
		b1 = b3 <<= 16;
		if (y3 < 0) {
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			b1 -= db2 * y3;
			b3 -= db3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		z2 <<= 16;
		b2 <<= 16;
		if (y2 < 0) {
			x2 -= dx1 * y2;
			z2 -= dz1 * y2;
			b2 -= db1 * y2;
			y2 = 0;
		}
		int l9 = y3 - center_y;
		l4 += j5 * l9;
		k5 += i6 * l9;
		j6 += l6 * l9;
		if (dx2 < dx3) {
			y1 -= y2;
			y2 -= y3;
			y3 = lineOffsets[y3];
			while (--y2 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x1 >> 16, x3 >> 16, z1, z3, b1 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
				x1 += dx2;
				x3 += dx3;
				z1 += dz2;
				z3 += dz3;
				b1 += db2;
				b3 += db3;
				y3 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y1 >= 0) {
				drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x2 >> 16, x3 >> 16, z2, z3, b2 >> 8, b3 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += dx1;
				x3 += dx3;
				z2 += dz1;
				z3 += dz3;
				b2 += db1;
				b3 += db3;
				y3 += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		y3 = lineOffsets[y3];
		while (--y2 >= 0) {
			drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x3 >> 16, x1 >> 16, z3, z1, b3 >> 8, b1 >> 8, l4, k5, j6, i5, l5, k6);
			x1 += dx2;
			x3 += dx3;
			z1 += dz2;
			z3 += dz3;
			b1 += db2;
			b3 += db3;
			y3 += DrawingArea.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
		while (--y1 >= 0) {
			drawDepthTexturesLine(DrawingArea.pixels, ai, y3, x3 >> 16, x2 >> 16, z3, z2, b3 >> 8, b2 >> 8, l4, k5, j6, i5, l5, k6);
			x2 += dx1;
			x3 += dx3;
			z2 += dz1;
			z3 += dz3;
			b2 += db1;
			b3 += db3;
			y3 += DrawingArea.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
	}

	private static boolean brighten;

	private static int brighten(int rgb) {
		if (!brighten) {
			return rgb;
		}
		int r = rgb >> 16 & 0xff;
		int g = rgb >> 8 & 0xff;
		int b = rgb & 0xff;
		// r = (r << 1) + 20;
		// g = (g << 1) + 20;
		// b = (b << 1) + 20;
		if (r > 0xff) r = 0xff;
		if (g > 0xff) g = 0xff;
		if (b > 0xff) b = 0xff;
		return r << 16 | g << 8 | b;
	}

	private static void drawDepthTexturesLine(int[] dst, int[] src, int off, int x1, int x2, int z1, int z2, int b1, int b2, int l1, int i2, int j2, int k2, int l2, int i3) {
		try {
			int i = 0;// was parameter
			int j = 0;// was parameter
			if (x1 >= x2)
				return;
			int dz = (z2 - z1 >> 16) / (x2 - x1);
			int db;
			int k3;
			if (restrict_edges) {
				db = (b2 - b1) / (x2 - x1);
				if (x2 > DrawingArea.viewportRX)
					x2 = DrawingArea.viewportRX;
				if (x1 < 0) {
					z1 -= x1 * dz;
					b1 -= x1 * db;
					x1 = 0;
				}
				if (x1 >= x2)
					return;
				k3 = x2 - x1 >> 3;
				db <<= 12;
				b1 <<= 9;
			} else {
				if (x2 - x1 > 7) {
					k3 = x2 - x1 >> 3;
				db = (b2 - b1) * shadowDecay[k3] >> 6;
				} else {
					k3 = 0;
					db = 0;
				}
				b1 <<= 9;
			}
			off += x1;
			if (lowMem) {
				int i4 = 0;
				int k4 = 0;
				int k6 = x1 - center_x;
				l1 += (k2 >> 3) * k6;
				i2 += (l2 >> 3) * k6;
				j2 += (i3 >> 3) * k6;
				int i5 = j2 >> 12;
				if (i5 != 0) {
					i = l1 / i5;
					j = i2 / i5;
					if (i < 0)
						i = 0;
					else if (i > 4032)
						i = 4032;
				}
				l1 += k2;
				i2 += l2;
				j2 += i3;
				i5 = j2 >> 12;
				if (i5 != 0) {
					i4 = l1 / i5;
					k4 = i2 / i5;
					if (i4 < 7)
						i4 = 7;
					else if (i4 > 4032)
						i4 = 4032;
				}
				int i7 = i4 - i >> 3;
				int k7 = k4 - j >> 3;
				i += (b1 & 0x600000) >> 3;
				int i8 = b1 >> 23;
				if (opaque) {
					while (k3-- > 0) {
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i = i4;
						j = k4;
						z1 += dz;
						l1 += k2;
						i2 += l2;
						j2 += i3;
						int j5 = j2 >> 12;
						if (j5 != 0) {
							i4 = l1 / j5;
							k4 = i2 / j5;
							if (i4 < 7)
								i4 = 7;
							else if (i4 > 4032)
								i4 = 4032;
						}
						i7 = i4 - i >> 3;
						k7 = k4 - j >> 3;
						b1 += db;
						i += (b1 & 0x600000) >> 3;
						i8 = b1 >> 23;
					}
					for (k3 = x2 - x1 & 7; k3-- > 0;) {
						depthBuffer[off] = z1;
						dst[off++] = src[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						z1 += dz;
					}

					return;
				}
				while (k3-- > 0) {
					int k8;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
					if ((k8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = k8;
						depthBuffer[off] = z1;
					}
					off++;
					i = i4;
					j = k4;
					z1 += dz;
					l1 += k2;
					i2 += l2;
					j2 += i3;
					int k5 = j2 >> 12;
					if (k5 != 0) {
						i4 = l1 / k5;
						k4 = i2 / k5;
						if (i4 < 7)
							i4 = 7;
						else if (i4 > 4032)
							i4 = 4032;
					}
					i7 = i4 - i >> 3;
					k7 = k4 - j >> 3;
					b1 += db;
					i += (b1 & 0x600000) >> 3;
					i8 = b1 >> 23;
				}
				for (k3 = x2 - x1 & 7; k3-- > 0;) {
					int l8;
					if ((l8 = src[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
						dst[off] = l8;
						depthBuffer[off] = z1;
					}
					off++;
					i += i7;
					j += k7;
					z1 += dz;
				}

				return;
			}
			int j4 = 0;
			int l4 = 0;
			int l6 = x1 - center_x;
			l1 += (k2 >> 3) * l6;
			i2 += (l2 >> 3) * l6;
			j2 += (i3 >> 3) * l6;
			int l5 = j2 >> 14;
			if (l5 != 0) {
				i = l1 / l5;
				j = i2 / l5;
				if (i < 0)
					i = 0;
				else if (i > 16256)
					i = 16256;
			}
			l1 += k2;
			i2 += l2;
			j2 += i3;
			l5 = j2 >> 14;
			if (l5 != 0) {
				j4 = l1 / l5;
				l4 = i2 / l5;
				if (j4 < 7)
					j4 = 7;
				else if (j4 > 16256)
					j4 = 16256;
			}
			int j7 = j4 - i >> 3;
			int l7 = l4 - j >> 3;
			i += b1 & 0x600000;
			int j8 = b1 >> 23;
			if (opaque) {
				while (k3-- > 0) {
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i = j4;
					j = l4;
					z1 += dz;
					l1 += k2;
					i2 += l2;
					j2 += i3;
					int i6 = j2 >> 14;
					if (i6 != 0) {
						j4 = l1 / i6;
						l4 = i2 / i6;
						if (j4 < 7)
							j4 = 7;
						else if (j4 > 16256)
							j4 = 16256;
					}
					j7 = j4 - i >> 3;
					l7 = l4 - j >> 3;
					b1 += db;
					i += b1 & 0x600000;
					j8 = b1 >> 23;
				}
				for (k3 = x2 - x1 & 7; k3-- > 0;) {
					depthBuffer[off] = z1;
					dst[off++] = src[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					z1 += dz;
				}

				return;
			}
			while (k3-- > 0) {
				int i9;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
				if ((i9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = i9;
					depthBuffer[off] = z1;
				}
				off++;
				i = j4;
				j = l4;
				z1 += dz;
				l1 += k2;
				i2 += l2;
				j2 += i3;
				int j6 = j2 >> 14;
				if (j6 != 0) {
					j4 = l1 / j6;
					l4 = i2 / j6;
					if (j4 < 7)
						j4 = 7;
					else if (j4 > 16256)
						j4 = 16256;
				}
				j7 = j4 - i >> 3;
				l7 = l4 - j >> 3;
				b1 += db;
				i += b1 & 0x600000;
				j8 = b1 >> 23;
			}
			for (int l3 = x2 - x1 & 7; l3-- > 0;) {
				int j9;
				if ((j9 = src[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
					dst[off] = j9;
					depthBuffer[off] = z1;
				}
				off++;
				i += j7;
				j += l7;
				z1 += dz;
			}
		} catch(Exception e) {e.printStackTrace();}
	}


	public static void drawShadedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2, 
			int hsl3)
	{
		int dx1 = 0;
		int dhsl1 = 0;
		if(y2 != y1)
		{
			dx1 = (x2 - x1 << 16) / (y2 - y1);
			dhsl1 = (hsl2 - hsl1 << 15) / (y2 - y1);
		}
		int dx2 = 0;
		int dhsl2 = 0;
		if(y3 != y2)
		{
			dx2 = (x3 - x2 << 16) / (y3 - y2);
			dhsl2 = (hsl3 - hsl2 << 15) / (y3 - y2);
		}
		int dx3 = 0;
		int dhsl3 = 0;
		if(y3 != y1)
		{
			dx3 = (x1 - x3 << 16) / (y1 - y3);
			dhsl3 = (hsl1 - hsl3 << 15) / (y1 - y3);
		}
		if(y1 <= y2 && y1 <= y3)
		{
			if(y1 >= DrawingArea.bottomY)
				return;
			if(y2 > DrawingArea.bottomY)
				y2 = DrawingArea.bottomY;
			if(y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if(y2 < y3)
			{
				x3 = x1 <<= 16;
				hsl3 = hsl1 <<= 15;
				if(y1 < 0)
				{
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					hsl3 -= dhsl3 * y1;
					hsl1 -= dhsl1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				hsl2 <<= 15;
				if(y2 < 0)
				{
					x2 -= dx2 * y2;
					hsl2 -= dhsl2 * y2;
					y2 = 0;
				}
				if(y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2)
				{
					y3 -= y2;
					y2 -= y1;
					for(y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width)
					{
						drawShadedLine(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
						x3 += dx3;
						x1 += dx1;
						hsl3 += dhsl3;
						hsl1 += dhsl1;
					}

					while(--y3 >= 0) 
					{
						drawShadedLine(DrawingArea.pixels, y1, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
						x3 += dx3;
						x2 += dx2;
						hsl3 += dhsl3;
						hsl2 += dhsl2;
						y1 += DrawingArea.width;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for(y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width)
				{
					drawShadedLine(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
					x3 += dx3;
					x1 += dx1;
					hsl3 += dhsl3;
					hsl1 += dhsl1;
				}

				while(--y3 >= 0) 
				{
					drawShadedLine(DrawingArea.pixels, y1, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
					x3 += dx3;
					x2 += dx2;
					hsl3 += dhsl3;
					hsl2 += dhsl2;
					y1 += DrawingArea.width;
				}
				return;
			}
			x2 = x1 <<= 16;
			hsl2 = hsl1 <<= 15;
			if(y1 < 0)
			{
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				hsl2 -= dhsl3 * y1;
				hsl1 -= dhsl1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			hsl3 <<= 15;
			if(y3 < 0)
			{
				x3 -= dx2 * y3;
				hsl3 -= dhsl2 * y3;
				y3 = 0;
			}
			if(y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1)
			{
				y2 -= y3;
				y3 -= y1;
				for(y1 = lineOffsets[y1]; --y3 >= 0; y1 += DrawingArea.width)
				{
					drawShadedLine(DrawingArea.pixels, y1, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
					x2 += dx3;
					x1 += dx1;
					hsl2 += dhsl3;
					hsl1 += dhsl1;
				}

				while(--y2 >= 0) 
				{
					drawShadedLine(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
					x3 += dx2;
					x1 += dx1;
					hsl3 += dhsl2;
					hsl1 += dhsl1;
					y1 += DrawingArea.width;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for(y1 = lineOffsets[y1]; --y3 >= 0; y1 += DrawingArea.width)
			{
				drawShadedLine(DrawingArea.pixels, y1, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
				x2 += dx3;
				x1 += dx1;
				hsl2 += dhsl3;
				hsl1 += dhsl1;
			}

			while(--y2 >= 0) 
			{
				drawShadedLine(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
				x3 += dx2;
				x1 += dx1;
				hsl3 += dhsl2;
				hsl1 += dhsl1;
				y1 += DrawingArea.width;
			}
			return;
		}
		if(y2 <= y3)
		{
			if(y2 >= DrawingArea.bottomY)
				return;
			if(y3 > DrawingArea.bottomY)
				y3 = DrawingArea.bottomY;
			if(y1 > DrawingArea.bottomY)
				y1 = DrawingArea.bottomY;
			if(y3 < y1)
			{
				x1 = x2 <<= 16;
				hsl1 = hsl2 <<= 15;
				if(y2 < 0)
				{
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					hsl1 -= dhsl1 * y2;
					hsl2 -= dhsl2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				hsl3 <<= 15;
				if(y3 < 0)
				{
					x3 -= dx3 * y3;
					hsl3 -= dhsl3 * y3;
					y3 = 0;
				}
				if(y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3)
				{
					y1 -= y3;
					y3 -= y2;
					for(y2 = lineOffsets[y2]; --y3 >= 0; y2 += DrawingArea.width)
					{
						drawShadedLine(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
						x1 += dx1;
						x2 += dx2;
						hsl1 += dhsl1;
						hsl2 += dhsl2;
					}

					while(--y1 >= 0) 
					{
						drawShadedLine(DrawingArea.pixels, y2, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
						x1 += dx1;
						x3 += dx3;
						hsl1 += dhsl1;
						hsl3 += dhsl3;
						y2 += DrawingArea.width;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for(y2 = lineOffsets[y2]; --y3 >= 0; y2 += DrawingArea.width)
				{
					drawShadedLine(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
					x1 += dx1;
					x2 += dx2;
					hsl1 += dhsl1;
					hsl2 += dhsl2;
				}

				while(--y1 >= 0) 
				{
					drawShadedLine(DrawingArea.pixels, y2, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
					x1 += dx1;
					x3 += dx3;
					hsl1 += dhsl1;
					hsl3 += dhsl3;
					y2 += DrawingArea.width;
				}
				return;
			}
			x3 = x2 <<= 16;
			hsl3 = hsl2 <<= 15;
			if(y2 < 0)
			{
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				hsl3 -= dhsl1 * y2;
				hsl2 -= dhsl2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0)
			{
				x1 -= dx3 * y1;
				hsl1 -= dhsl3 * y1;
				y1 = 0;
			}
			if(dx1 < dx2)
			{
				y3 -= y1;
				y1 -= y2;
				for(y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width)
				{
					drawShadedLine(DrawingArea.pixels, y2, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
					x3 += dx1;
					x2 += dx2;
					hsl3 += dhsl1;
					hsl2 += dhsl2;
				}

				while(--y3 >= 0) 
				{
					drawShadedLine(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
					x1 += dx3;
					x2 += dx2;
					hsl1 += dhsl3;
					hsl2 += dhsl2;
					y2 += DrawingArea.width;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for(y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width)
			{
				drawShadedLine(DrawingArea.pixels, y2, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
				x3 += dx1;
				x2 += dx2;
				hsl3 += dhsl1;
				hsl2 += dhsl2;
			}

			while(--y3 >= 0) 
			{
				drawShadedLine(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
				x1 += dx3;
				x2 += dx2;
				hsl1 += dhsl3;
				hsl2 += dhsl2;
				y2 += DrawingArea.width;
			}
			return;
		}
		if(y3 >= DrawingArea.bottomY)
			return;
		if(y1 > DrawingArea.bottomY)
			y1 = DrawingArea.bottomY;
		if(y2 > DrawingArea.bottomY)
			y2 = DrawingArea.bottomY;
		if(y1 < y2)
		{
			x2 = x3 <<= 16;
			hsl2 = hsl3 <<= 15;
			if(y3 < 0)
			{
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				hsl2 -= dhsl2 * y3;
				hsl3 -= dhsl3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0)
			{
				x1 -= dx1 * y1;
				hsl1 -= dhsl1 * y1;
				y1 = 0;
			}
			if(dx2 < dx3)
			{
				y2 -= y1;
				y1 -= y3;
				for(y3 = lineOffsets[y3]; --y1 >= 0; y3 += DrawingArea.width)
				{
					drawShadedLine(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
					x2 += dx2;
					x3 += dx3;
					hsl2 += dhsl2;
					hsl3 += dhsl3;
				}

				while(--y2 >= 0) 
				{
					drawShadedLine(DrawingArea.pixels, y3, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
					x2 += dx2;
					x1 += dx1;
					hsl2 += dhsl2;
					hsl1 += dhsl1;
					y3 += DrawingArea.width;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for(y3 = lineOffsets[y3]; --y1 >= 0; y3 += DrawingArea.width)
			{
				drawShadedLine(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
				x2 += dx2;
				x3 += dx3;
				hsl2 += dhsl2;
				hsl3 += dhsl3;
			}

			while(--y2 >= 0) 
			{
				drawShadedLine(DrawingArea.pixels, y3, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
				x2 += dx2;
				x1 += dx1;
				hsl2 += dhsl2;
				hsl1 += dhsl1;
				y3 += DrawingArea.width;
			}
			return;
		}
		x1 = x3 <<= 16;
		hsl1 = hsl3 <<= 15;
		if(y3 < 0)
		{
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			hsl1 -= dhsl2 * y3;
			hsl3 -= dhsl3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		hsl2 <<= 15;
		if(y2 < 0)
		{
			x2 -= dx1 * y2;
			hsl2 -= dhsl1 * y2;
			y2 = 0;
		}
		if(dx2 < dx3)
		{
			y1 -= y2;
			y2 -= y3;
			for(y3 = lineOffsets[y3]; --y2 >= 0; y3 += DrawingArea.width)
			{
				drawShadedLine(DrawingArea.pixels, y3, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
				x1 += dx2;
				x3 += dx3;
				hsl1 += dhsl2;
				hsl3 += dhsl3;
			}

			while(--y1 >= 0) 
			{
				drawShadedLine(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
				x2 += dx1;
				x3 += dx3;
				hsl2 += dhsl1;
				hsl3 += dhsl3;
				y3 += DrawingArea.width;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for(y3 = lineOffsets[y3]; --y2 >= 0; y3 += DrawingArea.width)
		{
			drawShadedLine(DrawingArea.pixels, y3, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
			x1 += dx2;
			x3 += dx3;
			hsl1 += dhsl2;
			hsl3 += dhsl3;
		}

		while(--y1 >= 0) 
		{
			drawShadedLine(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
			x2 += dx1;
			x3 += dx3;
			hsl2 += dhsl1;
			hsl3 += dhsl3;
			y3 += DrawingArea.width;
		}
	}

	private static void drawShadedLine(int ai[], int i, int l, int i1, int j1, int k1) {
		int j;// was parameter
		int k;// was parameter
		if (notTextured) {
			int l1;
			if (restrict_edges) {
				if (i1 - l > 3)
					l1 = (k1 - j1) / (i1 - l);
				else
					l1 = 0;
				if (i1 > DrawingArea.viewportRX)
					i1 = DrawingArea.viewportRX;
				if (l < 0) {
					j1 -= l * l1;
					l = 0;
				}
				if (l >= i1)
					return;
				i += l;
				k = i1 - l >> 2;
				l1 <<= 2;
			} else {
				if (l >= i1)
					return;
				i += l;
				k = i1 - l >> 2;
				if (k > 0)
					l1 = (k1 - j1) * shadowDecay[k] >> 15;
					else
						l1 = 0;
			}
			if (alpha == 0) {
				while (--k >= 0) {
					j = hsl2rgb[j1 >> 8];
					j1 += l1;
					ai[i++] = j;
					ai[i++] = j;
					ai[i++] = j;
					ai[i++] = j;
				}
				k = i1 - l & 3;
				if (k > 0) {
					try {
						j = hsl2rgb[j1 >> 8];
						do
							ai[i++] = j;
						while (--k > 0);
					} catch(Exception e) {}
					return;
				}
			} else {
				int j2 = alpha;
				int l2 = 256 - alpha;
				while (--k >= 0) {
					j = hsl2rgb[j1 >> 8];
					j1 += l1;
					j = ((j & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((j & 0xff00) * l2 >> 8 & 0xff00);
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
				}
				k = i1 - l & 3;// dugi
				if (k > 0) {
					j = hsl2rgb[j1 >> 8];
					j = ((j & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((j & 0xff00) * l2 >> 8 & 0xff00);
					do {
						ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
						i++;
					} while (--k > 0);
				}
			}
			return;
		}
		if (l >= i1)
			return;
		int i2 = (k1 - j1) / (i1 - l);
		if (restrict_edges) {
			if (i1 > DrawingArea.viewportRX)
				i1 = DrawingArea.viewportRX;
			if (l < 0) {
				j1 -= l * i2;
				l = 0;
			}
			if (l >= i1)
				return;
		}
		i += l;
		k = i1 - l;
		if (alpha == 0) {
			do {
				ai[i++] = hsl2rgb[j1 >> 8];
				j1 += i2;
			} while (--k > 0);
			return;
		}
		int k2 = alpha;
		int i3 = 256 - alpha;
		do {
			j = hsl2rgb[j1 >> 8];
			j1 += i2;
			j = ((j & 0xff00ff) * i3 >> 8 & 0xff00ff) + ((j & 0xff00) * i3 >> 8 & 0xff00);
			ai[i] = j + ((ai[i] & 0xff00ff) * k2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * k2 >> 8 & 0xff00);
			i++;
		} while (--k > 0);
	}

	public static void drawFlatTriangle(int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 0;
		if (j != i)
			l1 = (i1 - l << 16) / (j - i);
		int i2 = 0;
		if (k != j)
			i2 = (j1 - i1 << 16) / (k - j);
		int j2 = 0;
		if (k != i)
			j2 = (l - j1 << 16) / (i - k);
		if (i <= j && i <= k) {
			if (i >= DrawingArea.bottomY)
				return;
			if (j > DrawingArea.bottomY)
				j = DrawingArea.bottomY;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (j < k) {
				j1 = l <<= 16;
				if (i < 0) {
					j1 -= j2 * i;
					l -= l1 * i;
					i = 0;
				}
				i1 <<= 16;
				if (j < 0) {
					i1 -= i2 * j;
					j = 0;
				}
				if (i != j && j2 < l1 || i == j && j2 > i2) {
					k -= j;
					j -= i;
					for (i = lineOffsets[i]; --j >= 0; i += DrawingArea.width) {
						drawFlatLine(DrawingArea.pixels, i, k1, j1 >> 16, l >> 16);
						j1 += j2;
						l += l1;
					}

					while (--k >= 0) {
						drawFlatLine(DrawingArea.pixels, i, k1, j1 >> 16, i1 >> 16);
						j1 += j2;
						i1 += i2;
						i += DrawingArea.width;
					}
					return;
				}
				k -= j;
				j -= i;
				for (i = lineOffsets[i]; --j >= 0; i += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, i, k1, l >> 16, j1 >> 16);
					j1 += j2;
					l += l1;
				}

				while (--k >= 0) {
					drawFlatLine(DrawingArea.pixels, i, k1, i1 >> 16, j1 >> 16);
					j1 += j2;
					i1 += i2;
					i += DrawingArea.width;
				}
				return;
			}
			i1 = l <<= 16;
			if (i < 0) {
				i1 -= j2 * i;
				l -= l1 * i;
				i = 0;
			}
			j1 <<= 16;
			if (k < 0) {
				j1 -= i2 * k;
				k = 0;
			}
			if (i != k && j2 < l1 || i == k && i2 > l1) {
				j -= k;
				k -= i;
				for (i = lineOffsets[i]; --k >= 0; i += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, i, k1, i1 >> 16, l >> 16);
					i1 += j2;
					l += l1;
				}

				while (--j >= 0) {
					drawFlatLine(DrawingArea.pixels, i, k1, j1 >> 16, l >> 16);
					j1 += i2;
					l += l1;
					i += DrawingArea.width;
				}
				return;
			}
			j -= k;
			k -= i;
			for (i = lineOffsets[i]; --k >= 0; i += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, i, k1, l >> 16, i1 >> 16);
				i1 += j2;
				l += l1;
			}

			while (--j >= 0) {
				drawFlatLine(DrawingArea.pixels, i, k1, l >> 16, j1 >> 16);
				j1 += i2;
				l += l1;
				i += DrawingArea.width;
			}
			return;
		}
		if (j <= k) {
			if (j >= DrawingArea.bottomY)
				return;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (i > DrawingArea.bottomY)
				i = DrawingArea.bottomY;
			if (k < i) {
				l = i1 <<= 16;
				if (j < 0) {
					l -= l1 * j;
					i1 -= i2 * j;
					j = 0;
				}
				j1 <<= 16;
				if (k < 0) {
					j1 -= j2 * k;
					k = 0;
				}
				if (j != k && l1 < i2 || j == k && l1 > j2) {
					i -= k;
					k -= j;
					for (j = lineOffsets[j]; --k >= 0; j += DrawingArea.width) {
						drawFlatLine(DrawingArea.pixels, j, k1, l >> 16, i1 >> 16);
						l += l1;
						i1 += i2;
					}

					while (--i >= 0) {
						drawFlatLine(DrawingArea.pixels, j, k1, l >> 16, j1 >> 16);
						l += l1;
						j1 += j2;
						j += DrawingArea.width;
					}
					return;
				}
				i -= k;
				k -= j;
				for (j = lineOffsets[j]; --k >= 0; j += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, j, k1, i1 >> 16, l >> 16);
					l += l1;
					i1 += i2;
				}

				while (--i >= 0) {
					drawFlatLine(DrawingArea.pixels, j, k1, j1 >> 16, l >> 16);
					l += l1;
					j1 += j2;
					j += DrawingArea.width;
				}
				return;
			}
			j1 = i1 <<= 16;
			if (j < 0) {
				j1 -= l1 * j;
				i1 -= i2 * j;
				j = 0;
			}
			l <<= 16;
			if (i < 0) {
				l -= j2 * i;
				i = 0;
			}
			if (l1 < i2) {
				k -= i;
				i -= j;
				for (j = lineOffsets[j]; --i >= 0; j += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, j, k1, j1 >> 16, i1 >> 16);
					j1 += l1;
					i1 += i2;
				}

				while (--k >= 0) {
					drawFlatLine(DrawingArea.pixels, j, k1, l >> 16, i1 >> 16);
					l += j2;
					i1 += i2;
					j += DrawingArea.width;
				}
				return;
			}
			k -= i;
			i -= j;
			for (j = lineOffsets[j]; --i >= 0; j += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, j, k1, i1 >> 16, j1 >> 16);
				j1 += l1;
				i1 += i2;
			}

			while (--k >= 0) {
				drawFlatLine(DrawingArea.pixels, j, k1, i1 >> 16, l >> 16);
				l += j2;
				i1 += i2;
				j += DrawingArea.width;
			}
			return;
		}
		if (k >= DrawingArea.bottomY)
			return;
		if (i > DrawingArea.bottomY)
			i = DrawingArea.bottomY;
		if (j > DrawingArea.bottomY)
			j = DrawingArea.bottomY;
		if (i < j) {
			i1 = j1 <<= 16;
			if (k < 0) {
				i1 -= i2 * k;
				j1 -= j2 * k;
				k = 0;
			}
			l <<= 16;
			if (i < 0) {
				l -= l1 * i;
				i = 0;
			}
			if (i2 < j2) {
				j -= i;
				i -= k;
				for (k = lineOffsets[k]; --i >= 0; k += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, k, k1, i1 >> 16, j1 >> 16);
					i1 += i2;
					j1 += j2;
				}

				while (--j >= 0) {
					drawFlatLine(DrawingArea.pixels, k, k1, i1 >> 16, l >> 16);
					i1 += i2;
					l += l1;
					k += DrawingArea.width;
				}
				return;
			}
			j -= i;
			i -= k;
			for (k = lineOffsets[k]; --i >= 0; k += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, k, k1, j1 >> 16, i1 >> 16);
				i1 += i2;
				j1 += j2;
			}

			while (--j >= 0) {
				drawFlatLine(DrawingArea.pixels, k, k1, l >> 16, i1 >> 16);
				i1 += i2;
				l += l1;
				k += DrawingArea.width;
			}
			return;
		}
		l = j1 <<= 16;
		if (k < 0) {
			l -= i2 * k;
			j1 -= j2 * k;
			k = 0;
		}
		i1 <<= 16;
		if (j < 0) {
			i1 -= l1 * j;
			j = 0;
		}
		if (i2 < j2) {
			i -= j;
			j -= k;
			for (k = lineOffsets[k]; --j >= 0; k += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, k, k1, l >> 16, j1 >> 16);
				l += i2;
				j1 += j2;
			}

			while (--i >= 0) {
				drawFlatLine(DrawingArea.pixels, k, k1, i1 >> 16, j1 >> 16);
				i1 += l1;
				j1 += j2;
				k += DrawingArea.width;
			}
			return;
		}
		i -= j;
		j -= k;
		for (k = lineOffsets[k]; --j >= 0; k += DrawingArea.width) {
			drawFlatLine(DrawingArea.pixels, k, k1, j1 >> 16, l >> 16);
			l += i2;
			j1 += j2;
		}

		while (--i >= 0) {
			drawFlatLine(DrawingArea.pixels, k, k1, j1 >> 16, i1 >> 16);
			i1 += l1;
			j1 += j2;
			k += DrawingArea.width;
		}
	}

	private static void drawFlatLine(int ai[], int i, int j, int l, int i1) {
		int k;// was parameter
		if (restrict_edges) {
			if (i1 > DrawingArea.viewportRX)
				i1 = DrawingArea.viewportRX;
			if (l < 0)
				l = 0;
		}
		if (l >= i1)
			return;
		i += l;
		k = i1 - l >> 2;
		if (alpha == 0) {
			while (--k >= 0) {
				ai[i++] = j;
				ai[i++] = j;
				ai[i++] = j;
				ai[i++] = j;
			}
			for (k = i1 - l & 3; --k >= 0;)
				ai[i++] = j;

			return;
		}
		int j1 = alpha;
		int k1 = 256 - alpha;
		j = ((j & 0xff00ff) * k1 >> 8 & 0xff00ff) + ((j & 0xff00) * k1 >> 8 & 0xff00);
		while (--k >= 0) {
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
		}
		for (k = i1 - l & 3; --k >= 0;)
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);

	}

	public static void drawTexturedTriangle(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4) {
		int ai[] = null;
		if(Client.getOption("hd_tex")) {
			ai = TextureLoader667.getTexturePixels(k4);
		} else {
			ai = TextureLoader317.getTexturePixels(k4);
		}
		opaque = false;
		if(!Client.getOption("hd_tex"))
			opaque = !TextureLoader317.textureIsTransparent[k4];
		k2 = j2 - k2;
		j3 = i3 - j3;
		i4 = l3 - i4;
		l2 -= j2;
		k3 -= i3;
		j4 -= l3;
		int l4 = (l2 * i3 - k3 * j2) * WorldController.focalLength << 5;
		int i5 = k3 * l3 - j4 * i3 << 8;
		int j5 = j4 * j2 - l2 * l3 << 5;
		int k5 = (k2 * i3 - j3 * j2) * WorldController.focalLength << 5;
		int l5 = j3 * l3 - i4 * i3 << 8;
		int i6 = i4 * j2 - k2 * l3 << 5;
		int j6 = (j3 * l2 - k2 * k3) * WorldController.focalLength << 5;
		int k6 = i4 * k3 - j3 * j4 << 8;
		int l6 = k2 * j4 - i4 * l2 << 5;
		int i7 = 0;
		int j7 = 0;
		if (j != i) {
			i7 = (i1 - l << 16) / (j - i);
			j7 = (l1 - k1 << 16) / (j - i);
		}
		int k7 = 0;
		int l7 = 0;
		if (k != j) {
			k7 = (j1 - i1 << 16) / (k - j);
			l7 = (i2 - l1 << 16) / (k - j);
		}
		int i8 = 0;
		int j8 = 0;
		if (k != i) {
			i8 = (l - j1 << 16) / (i - k);
			j8 = (k1 - i2 << 16) / (i - k);
		}
		if (i <= j && i <= k) {
			if (i >= DrawingArea.bottomY)
				return;
			if (j > DrawingArea.bottomY)
				j = DrawingArea.bottomY;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (j < k) {
				j1 = l <<= 16;
				i2 = k1 <<= 16;
				if (i < 0) {
					j1 -= i8 * i;
					l -= i7 * i;
					i2 -= j8 * i;
					k1 -= j7 * i;
					i = 0;
				}
				i1 <<= 16;
				l1 <<= 16;
				if (j < 0) {
					i1 -= k7 * j;
					l1 -= l7 * j;
					j = 0;
				}
				int k8 = i - center_y;
				l4 += j5 * k8;
				k5 += i6 * k8;
				j6 += l6 * k8;
				if (i != j && i8 < i7 || i == j && i8 > k7) {
					k -= j;
					j -= i;
					i = lineOffsets[i];
					while (--j >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, i, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
						j1 += i8;
						l += i7;
						i2 += j8;
						k1 += j7;
						i += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--k >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, i, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
						j1 += i8;
						i1 += k7;
						i2 += j8;
						l1 += l7;
						i += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				k -= j;
				j -= i;
				i = lineOffsets[i];
				while (--j >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += i8;
					l += i7;
					i2 += j8;
					k1 += j7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += i8;
					i1 += k7;
					i2 += j8;
					l1 += l7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			i1 = l <<= 16;
			l1 = k1 <<= 16;
			if (i < 0) {
				i1 -= i8 * i;
				l -= i7 * i;
				l1 -= j8 * i;
				k1 -= j7 * i;
				i = 0;
			}
			j1 <<= 16;
			i2 <<= 16;
			if (k < 0) {
				j1 -= k7 * k;
				i2 -= l7 * k;
				k = 0;
			}
			int l8 = i - center_y;
			l4 += j5 * l8;
			k5 += i6 * l8;
			j6 += l6 * l8;
			if (i != k && i8 < i7 || i == k && k7 > i7) {
				j -= k;
				k -= i;
				i = lineOffsets[i];
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					i1 += i8;
					l += i7;
					l1 += j8;
					k1 += j7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--j >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += k7;
					l += i7;
					i2 += l7;
					k1 += j7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			j -= k;
			k -= i;
			i = lineOffsets[i];
			while (--k >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, i, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += i8;
				l += i7;
				l1 += j8;
				k1 += j7;
				i += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--j >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, i, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				j1 += k7;
				l += i7;
				i2 += l7;
				k1 += j7;
				i += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (j <= k) {
			if (j >= DrawingArea.bottomY)
				return;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (i > DrawingArea.bottomY)
				i = DrawingArea.bottomY;
			if (k < i) {
				l = i1 <<= 16;
				k1 = l1 <<= 16;
				if (j < 0) {
					l -= i7 * j;
					i1 -= k7 * j;
					k1 -= j7 * j;
					l1 -= l7 * j;
					j = 0;
				}
				j1 <<= 16;
				i2 <<= 16;
				if (k < 0) {
					j1 -= i8 * k;
					i2 -= j8 * k;
					k = 0;
				}
				int i9 = j - center_y;
				l4 += j5 * i9;
				k5 += i6 * i9;
				j6 += l6 * i9;
				if (j != k && i7 < k7 || j == k && i7 > i8) {
					i -= k;
					k -= j;
					j = lineOffsets[j];
					while (--k >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, j, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
						l += i7;
						i1 += k7;
						k1 += j7;
						l1 += l7;
						j += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--i >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, j, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
						l += i7;
						j1 += i8;
						k1 += j7;
						i2 += j8;
						j += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				i -= k;
				k -= j;
				j = lineOffsets[j];
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					l += i7;
					i1 += k7;
					k1 += j7;
					l1 += l7;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--i >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					l += i7;
					j1 += i8;
					k1 += j7;
					i2 += j8;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			j1 = i1 <<= 16;
			i2 = l1 <<= 16;
			if (j < 0) {
				j1 -= i7 * j;
				i1 -= k7 * j;
				i2 -= j7 * j;
				l1 -= l7 * j;
				j = 0;
			}
			l <<= 16;
			k1 <<= 16;
			if (i < 0) {
				l -= i8 * i;
				k1 -= j8 * i;
				i = 0;
			}
			int j9 = j - center_y;
			l4 += j5 * j9;
			k5 += i6 * j9;
			j6 += l6 * j9;
			if (i7 < k7) {
				k -= i;
				i -= j;
				j = lineOffsets[j];
				while (--i >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += i7;
					i1 += k7;
					i2 += j7;
					l1 += l7;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
					l += i8;
					i1 += k7;
					k1 += j8;
					l1 += l7;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			k -= i;
			i -= j;
			j = lineOffsets[j];
			while (--i >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, j, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				j1 += i7;
				i1 += k7;
				i2 += j7;
				l1 += l7;
				j += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--k >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, j, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
				l += i8;
				i1 += k7;
				k1 += j8;
				l1 += l7;
				j += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (k >= DrawingArea.bottomY)
			return;
		if (i > DrawingArea.bottomY)
			i = DrawingArea.bottomY;
		if (j > DrawingArea.bottomY)
			j = DrawingArea.bottomY;
		if (i < j) {
			i1 = j1 <<= 16;
			l1 = i2 <<= 16;
			if (k < 0) {
				i1 -= k7 * k;
				j1 -= i8 * k;
				l1 -= l7 * k;
				i2 -= j8 * k;
				k = 0;
			}
			l <<= 16;
			k1 <<= 16;
			if (i < 0) {
				l -= i7 * i;
				k1 -= j7 * i;
				i = 0;
			}
			int k9 = k - center_y;
			l4 += j5 * k9;
			k5 += i6 * k9;
			j6 += l6 * k9;
			if (k7 < i8) {
				j -= i;
				i -= k;
				k = lineOffsets[k];
				while (--i >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, k, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
					i1 += k7;
					j1 += i8;
					l1 += l7;
					i2 += j8;
					k += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--j >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, k, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					i1 += k7;
					l += i7;
					l1 += l7;
					k1 += j7;
					k += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			j -= i;
			i -= k;
			k = lineOffsets[k];
			while (--i >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += k7;
				j1 += i8;
				l1 += l7;
				i2 += j8;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--j >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += k7;
				l += i7;
				l1 += l7;
				k1 += j7;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		l = j1 <<= 16;
		k1 = i2 <<= 16;
		if (k < 0) {
			l -= k7 * k;
			j1 -= i8 * k;
			k1 -= l7 * k;
			i2 -= j8 * k;
			k = 0;
		}
		i1 <<= 16;
		l1 <<= 16;
		if (j < 0) {
			i1 -= i7 * j;
			l1 -= j7 * j;
			j = 0;
		}
		int l9 = k - center_y;
		l4 += j5 * l9;
		k5 += i6 * l9;
		j6 += l6 * l9;
		if (k7 < i8) {
			i -= j;
			j -= k;
			k = lineOffsets[k];
			while (--j >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				l += k7;
				j1 += i8;
				k1 += l7;
				i2 += j8;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--i >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += i7;
				j1 += i8;
				l1 += j7;
				i2 += j8;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		i -= j;
		j -= k;
		k = lineOffsets[k];
		while (--j >= 0) {
			drawTexturesLine(DrawingArea.pixels, ai, k, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
			l += k7;
			j1 += i8;
			k1 += l7;
			i2 += j8;
			k += DrawingArea.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
		while (--i >= 0) {
			drawTexturesLine(DrawingArea.pixels, ai, k, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
			i1 += i7;
			j1 += i8;
			l1 += j7;
			i2 += j8;
			k += DrawingArea.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
	}

	private static void drawTexturesLine(int ai[], int ai1[], int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
		try {
			int i = 0;// was parameter
			int j = 0;// was parameter
			if (l >= i1)
				return;
			int j3;
			int k3;
			if (restrict_edges) {
				j3 = (k1 - j1) / (i1 - l);
				if (i1 > DrawingArea.viewportRX)
					i1 = DrawingArea.viewportRX;
				if (l < 0) {
					j1 -= l * j3;
					l = 0;
				}
				if (l >= i1)
					return;
				k3 = i1 - l >> 3;
				j3 <<= 12;
				j1 <<= 9;
			} else {
				if (i1 - l > 7) {
					k3 = i1 - l >> 3;
				j3 = (k1 - j1) * shadowDecay[k3] >> 6;
				} else {
					k3 = 0;
					j3 = 0;
				}
				j1 <<= 9;
			}
			k += l;
			if (lowMem) {
				int i4 = 0;
				int k4 = 0;
				int k6 = l - center_x;
				l1 += (k2 >> 3) * k6;
				i2 += (l2 >> 3) * k6;
				j2 += (i3 >> 3) * k6;
				int i5 = j2 >> 12;
				if (i5 != 0) {
					i = l1 / i5;
					j = i2 / i5;
					if (i < 0)
						i = 0;
					else if (i > 4032)
						i = 4032;
				}
				l1 += k2;
				i2 += l2;
				j2 += i3;
				i5 = j2 >> 12;
				if (i5 != 0) {
					i4 = l1 / i5;
					k4 = i2 / i5;
					if (i4 < 7)
						i4 = 7;
					else if (i4 > 4032)
						i4 = 4032;
				}
				int i7 = i4 - i >> 3;
				int k7 = k4 - j >> 3;
				i += (j1 & 0x600000) >> 3;
				int i8 = j1 >> 23;
				if (opaque) {
					while (k3-- > 0) {
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i = i4;
						j = k4;
						l1 += k2;
						i2 += l2;
						j2 += i3;
						int j5 = j2 >> 12;
						if (j5 != 0) {
							i4 = l1 / j5;
							k4 = i2 / j5;
							if (i4 < 7)
								i4 = 7;
							else if (i4 > 4032)
								i4 = 4032;
						}
						i7 = i4 - i >> 3;
						k7 = k4 - j >> 3;
						j1 += j3;
						i += (j1 & 0x600000) >> 3;
						i8 = j1 >> 23;
					}
					for (k3 = i1 - l & 7; k3-- > 0;) {
						ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
						i += i7;
						j += k7;
					}

					return;
				}
				while (k3-- > 0) {
					int k8;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i += i7;
					j += k7;
					if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = k8;
					k++;
					i = i4;
					j = k4;
					l1 += k2;
					i2 += l2;
					j2 += i3;
					int k5 = j2 >> 12;
					if (k5 != 0) {
						i4 = l1 / k5;
						k4 = i2 / k5;
						if (i4 < 7)
							i4 = 7;
						else if (i4 > 4032)
							i4 = 4032;
					}
					i7 = i4 - i >> 3;
					k7 = k4 - j >> 3;
					j1 += j3;
					i += (j1 & 0x600000) >> 3;
					i8 = j1 >> 23;
				}
				for (k3 = i1 - l & 7; k3-- > 0;) {
					int l8;
					if ((l8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
						ai[k] = l8;
					k++;
					i += i7;
					j += k7;
				}

				return;
			}
			int j4 = 0;
			int l4 = 0;
			int l6 = l - center_x;
			l1 += (k2 >> 3) * l6;
			i2 += (l2 >> 3) * l6;
			j2 += (i3 >> 3) * l6;
			int l5 = j2 >> 14;
			if (l5 != 0) {
				i = l1 / l5;
				j = i2 / l5;
				if (i < 0)
					i = 0;
				else if (i > 16256)
					i = 16256;
			}
			l1 += k2;
			i2 += l2;
			j2 += i3;
			l5 = j2 >> 14;
			if (l5 != 0) {
				j4 = l1 / l5;
				l4 = i2 / l5;
				if (j4 < 7)
					j4 = 7;
				else if (j4 > 16256)
					j4 = 16256;
			}
			int j7 = j4 - i >> 3;
			int l7 = l4 - j >> 3;
			i += j1 & 0x600000;
			int j8 = j1 >> 23;
			if (opaque) {
				while (k3-- > 0) {
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i = j4;
					j = l4;
					l1 += k2;
					i2 += l2;
					j2 += i3;
					int i6 = j2 >> 14;
					if (i6 != 0) {
						j4 = l1 / i6;
						l4 = i2 / i6;
						if (j4 < 7)
							j4 = 7;
						else if (j4 > 16256)
							j4 = 16256;
					}
					j7 = j4 - i >> 3;
					l7 = l4 - j >> 3;
					j1 += j3;
					i += j1 & 0x600000;
					j8 = j1 >> 23;
				}
				for (k3 = i1 - l & 7; k3-- > 0;) {
					ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
					i += j7;
					j += l7;
				}

				return;
			}
			while (k3-- > 0) {
				int i9;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i += j7;
				j += l7;
				if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = i9;
				k++;
				i = j4;
				j = l4;
				l1 += k2;
				i2 += l2;
				j2 += i3;
				int j6 = j2 >> 14;
				if (j6 != 0) {
					j4 = l1 / j6;
					l4 = i2 / j6;
					if (j4 < 7)
						j4 = 7;
					else if (j4 > 16256)
						j4 = 16256;
				}
				j7 = j4 - i >> 3;
				l7 = l4 - j >> 3;
				j1 += j3;
				i += j1 & 0x600000;
				j8 = j1 >> 23;
			}
			for (int l3 = i1 - l & 7; l3-- > 0;) {
				int j9;
				if ((j9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
					ai[k] = j9;
				k++;
				i += j7;
				j += l7;
			}
		} catch(Exception e) {e.printStackTrace();}
	}

	private static int[] OFFSETS_512_334 = null;
	private static int[] OFFSETS_765_503 = null;

	public static int[] getOffsets(int j, int k) {
		if (j == 512 && k == 334 && OFFSETS_512_334 != null)
			return OFFSETS_512_334;

		if (j == 765 + 1 && k == 503 && OFFSETS_765_503 != null)
			return OFFSETS_765_503;

		int[] t = new int[k];
		for (int l = 0; l < k; l++)
			t[l] = j * l;

		if (j == 512 && k == 334)
			OFFSETS_512_334 = t;

		if (j == 765 + 1 && k == 503)
			OFFSETS_765_503 = t;

		return t;
	}

	public static void clearDepthBuffer() {
		if (!saveDepth && depthBuffer != null) {
			depthBuffer = null;
			return;
		}
		if (depthBuffer == null || depthBuffer.length != DrawingArea.pixels.length) {
			depthBuffer = new int[DrawingArea.pixels.length];
		}
		int pos = 0;
		for (int i = 0, n = depthBuffer.length >> 3; i < n; i++) {
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
			depthBuffer[pos++] = 0x7fffffff;
		}
		for (int i = 0, n = depthBuffer.length & 7; i < n; i++) {
			depthBuffer[pos++] = 0x7fffffff;
		}
	}

	public static void drawFog(int beginDepth, int endDepth, int rgb) {
		if (!saveDepth) {
			return;
		}
		int x = (Client.getBaseX() + (Client.myPlayer.x - 6 >> 7)) >> 3;
		int y = (Client.getBaseY() + (Client.myPlayer.y - 6 >> 7)) >> 3;
		boolean colorchange = (!(x >= 270 && x <= 465 && y >= 335 && y <= 495));
		if(colorchange) {
			rgb = 0;
		}
		beginDepth <<= 16;
		endDepth <<= 16;
		int fogIntensity = endDepth - beginDepth >> 8;
		for (int i = depthBuffer.length - 1; i >= 0; i--) {
			if (depthBuffer[i] >= endDepth) {
				DrawingArea.pixels[i] = rgb;
			} else if (depthBuffer[i] >= beginDepth) {
				int a = (depthBuffer[i] - beginDepth) / fogIntensity;
				int src = ((rgb & 0xff00ff) * a >> 8 & 0xff00ff) + ((rgb & 0xff00) * a >> 8 & 0xff00);
				a = 256 - a;
				int dst = DrawingArea.pixels[i];
				dst = ((dst & 0xff00ff) * a >> 8 & 0xff00ff) + ((dst & 0xff00) * a >> 8 & 0xff00);
				DrawingArea.pixels[i] = src + dst;
			}
		}
	}

	public static final int anInt1459 = -477;
	public static boolean lowMem = true;
	static boolean restrict_edges;
	private static boolean opaque;
	public static boolean notTextured = true;
	public static int alpha;
	public static int center_x;
	public static int center_y;
	private static int[] shadowDecay;
	public static final int[] light_decay;
	public static int SINE[];
	public static int COSINE[];
	public static int lineOffsets[];
	public static int hsl2rgb[] = new int[0x10000];
	public static int[] depthBuffer;
	public static boolean saveDepth;

	static {
		shadowDecay = new int[512];
		light_decay = new int[2048];
		SINE = new int[2048];
		COSINE = new int[2048];
		for (int i = 1; i < 512; i++)
			shadowDecay[i] = 32768 / i;

		for (int j = 1; j < 2048; j++)
			light_decay[j] = 0x10000 / j;

		for (int k = 0; k < 2048; k++) {
			SINE[k] = (int) (65536D * Math.sin((double) k * 0.0030679614999999999D));
			COSINE[k] = (int) (65536D * Math.cos((double) k * 0.0030679614999999999D));
		}

	}

	
    public static void drawTexturedTriangle_model(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int z_a, int z_b, int z_c, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int My, int Nz, int Py, int Mz, int Ny, int t_id, int color, boolean force) {
        drawTexturedTriangle_model(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b, z_c, grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, My, Nz, Py, Mz, Ny, t_id, color, force, false);
    }

    public static void drawTexturedTriangle_model(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int z_a, int z_b, int z_c, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int My, int Nz, int Py, int Mz, int Ny, int t_id, int color, boolean force, boolean floor) {
    	render_texture_triangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b, z_c, grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, My, Nz, Py, Mz, Ny, t_id, color, force, true, floor);
    }

	public static int triangles = 0;

	public static void render_texture_triangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int z_a, int z_b, int z_c, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int My, int Nz, int Py, int Mz, int Ny, int t_id, int color, boolean force, boolean floor, boolean isFloor) {
		if (t_id < 0 || t_id >= TextureDef.textures.length)
		{
			drawDepthShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b, z_c, grad_a, grad_b, grad_c);
			return;
		}

		TextureDef def = TextureDef.textures[t_id];
		if (def == null)
		{
			drawDepthShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b, z_c, grad_a, grad_b, grad_c);
			return;
		}

		if (!force && !def.aBoolean1223 && lowMem)
		{
			drawDepthShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b, z_c, grad_a, grad_b, grad_c);
			return;
		}

		int texture[] = null;
		if(Client.getOption("hd_tex")) {
			texture = TextureLoader667.getTexturePixels(t_id);
		} else {
			texture = TextureLoader317.getTexturePixels(t_id);
		}
		if (texture == null)
		{
			drawDepthShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b, z_c, grad_a, grad_b, grad_c);
			return;
		}

        if (color >= 0xffff)
            color = -1;

        if (color >= 0 && color < 65535) {
            color = hsl2rgb[color];
        }

        Mx = Px - Mx;
        My = Pz - My;
        Mz = Py - Mz;
        Nx -= Px;
        Nz -= Pz;
        Ny -= Py;
        int Oa = (Nx * Pz - Nz * Px) * WorldController.focalLength << 5;
        int Ha = Nz * Py - Ny * Pz << 8;
        int Va = Ny * Px - Nx * Py << 5;
        int Ob = (Mx * Pz - My * Px) * WorldController.focalLength << 5;
        int Hb = My * Py - Mz * Pz << 8;
        int Vb = Mz * Px - Mx * Py << 5;
        int Oc = (My * Nx - Mx * Nz) * WorldController.focalLength << 5;
        int Hc = Mz * Nz - My * Ny << 8;
        int Vc = Mx * Ny - Mz * Nx << 5;


        int x_a_off = 0;
        int z_a_off = 0;
        int grad_a_off = 0;

        int col_a = grad_a;
        int col_b = grad_b;
        int col_c = grad_c;
        int col_a_off = 0;
        int col_b_off = 0;
        int col_c_off = 0;

        if (y_b != y_a) {
            x_a_off = (x_b - x_a << 16) / (y_b - y_a);
            z_a_off = (z_b - z_a << 16) / (y_b - y_a);
            grad_a_off = (grad_b - grad_a << 16) / (y_b - y_a);
            col_a_off = (col_b - col_a << 15) / (y_b - y_a);
        }
        int x_b_off = 0;
        int z_b_off = 0;
        int grad_b_off = 0;
        if (y_c != y_b) {
            x_b_off = (x_c - x_b << 16) / (y_c - y_b);
            z_b_off = (z_c - z_b << 16) / (y_c - y_b);
            grad_b_off = (grad_c - grad_b << 16) / (y_c - y_b);
            col_b_off = (col_c - col_b << 15) / (y_c - y_b);
        }
        int x_c_off = 0;
        int z_c_off = 0;
        int grad_c_off = 0;
        if (y_c != y_a) {
            x_c_off = (x_a - x_c << 16) / (y_a - y_c);
            z_c_off = (z_a - z_c << 16) / (y_a - y_c);
            grad_c_off = (grad_a - grad_c << 16) / (y_a - y_c);
            col_c_off = (col_a - col_c << 15) / (y_a - y_c);
        }
        if (y_a <= y_b && y_a <= y_c) {
            if (y_a >= bottomY)
                return;
            if (y_b > bottomY)
                y_b = bottomY;
            if (y_c > bottomY)
                y_c = bottomY;
            if (y_b < y_c) {
                x_c = x_a <<= 16;
                z_c = z_a <<= 16;
                grad_c = grad_a <<= 16;
                col_c = col_a <<= 15;
                if (y_a < 0) {
                    x_c -= x_c_off * y_a;
                    x_a -= x_a_off * y_a;
                    z_c -= z_c_off * y_a;
                    z_a -= z_a_off * y_a;
                    grad_c -= grad_c_off * y_a;
                    grad_a -= grad_a_off * y_a;
                    col_c -= col_c_off * y_a;
                    col_a -= col_a_off * y_a;
                    y_a = 0;
                }
                x_b <<= 16;
                z_b <<= 16;
                grad_b <<= 16;
                col_b <<= 15;
                if (y_b < 0) {
                    x_b -= x_b_off * y_b;
                    z_b -= z_b_off * y_b;
                    grad_b -= grad_b_off * y_b;
                    col_b -= col_b_off * y_b;
                    y_b = 0;
                }
                int jA = y_a - center_y;
                Oa += Va * jA;
                Ob += Vb * jA;
                Oc += Vc * jA;
                if (y_a != y_b && x_c_off < x_a_off || y_a == y_b
                        && x_c_off > x_b_off) {
                    y_c -= y_b;
                    y_b -= y_a;
                    y_a = lineOffsets[y_a];
                    while (--y_b >= 0) {
                        drawTexturedScanline(pixels, texture, y_a, x_c >> 16,
                                x_a >> 16, grad_c >> 8, grad_a >> 8,
                                col_c >> 7, col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                color, force, floor, z_c, z_a);
                        x_c += x_c_off;
                        x_a += x_a_off;
                        z_c += z_c_off;
                        z_a += z_a_off;
                        grad_c += grad_c_off;
                        grad_a += grad_a_off;
                        col_c += col_c_off;
                        col_a += col_a_off;
                        y_a += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    while (--y_c >= 0) {
                        drawTexturedScanline(pixels, texture, y_a, x_c >> 16,
                                x_b >> 16, grad_c >> 8, grad_b >> 8,
                                col_c >> 7, col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                color, force, floor, z_c, z_b);
                        x_c += x_c_off;
                        x_b += x_b_off;
                        z_c += z_c_off;
                        z_b += z_b_off;
                        grad_c += grad_c_off;
                        grad_b += grad_b_off;
                        col_c += col_c_off;
                        col_b += col_b_off;
                        y_a += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    return;
                }
                y_c -= y_b;
                y_b -= y_a;
                y_a = lineOffsets[y_a];
                while (--y_b >= 0) {
                    drawTexturedScanline(pixels, texture, y_a, x_a >> 16,
                            x_c >> 16, grad_a >> 8, grad_c >> 8, col_a >> 7,
                            col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_a, z_c);
                    x_c += x_c_off;
                    x_a += x_a_off;
                    z_c += z_c_off;
                    z_a += z_a_off;
                    grad_c += grad_c_off;
                    grad_a += grad_a_off;
                    col_c += col_c_off;
                    col_a += col_a_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_c >= 0) {
                    drawTexturedScanline(pixels, texture, y_a, x_b >> 16,
                            x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                            col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_b, z_c);
                    x_c += x_c_off;
                    x_b += x_b_off;
                    z_c += z_c_off;
                    z_b += z_b_off;
                    grad_c += grad_c_off;
                    grad_b += grad_b_off;
                    col_c += col_c_off;
                    col_b += col_b_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            x_b = x_a <<= 16;
            z_b = z_a <<= 16;
            grad_b = grad_a <<= 16;
            col_b = col_a <<= 15;
            if (y_a < 0) {
                x_b -= x_c_off * y_a;
                x_a -= x_a_off * y_a;
                z_b -= z_c_off * y_a;
                z_a -= z_a_off * y_a;
                grad_b -= grad_c_off * y_a;
                grad_a -= grad_a_off * y_a;
                col_b -= col_c_off * y_a;
                col_a -= col_a_off * y_a;
                y_a = 0;
            }
            x_c <<= 16;
            z_c <<= 16;
            grad_c <<= 16;
            col_c <<= 15;
            if (y_c < 0) {
                x_c -= x_b_off * y_c;
                z_c -= z_b_off * y_c;
                grad_c -= grad_b_off * y_c;
                col_c -= col_b_off * y_c;
                y_c = 0;
            }
            int l8 = y_a - center_y;
            Oa += Va * l8;
            Ob += Vb * l8;
            Oc += Vc * l8;
            if (y_a != y_c && x_c_off < x_a_off || y_a == y_c
                    && x_b_off > x_a_off) {
                y_b -= y_c;
                y_c -= y_a;
                y_a = lineOffsets[y_a];
                while (--y_c >= 0) {
                    drawTexturedScanline(pixels, texture, y_a, x_b >> 16,
                            x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_b, z_a);
                    x_b += x_c_off;
                    x_a += x_a_off;
                    z_b += z_c_off;
                    z_a += z_a_off;
                    grad_b += grad_c_off;
                    grad_a += grad_a_off;
                    col_b += col_c_off;
                    col_a += col_a_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_b >= 0) {
                    drawTexturedScanline(pixels, texture, y_a, x_c >> 16,
                            x_a >> 16, grad_c >> 8, grad_a >> 8, col_c >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_c, z_a);
                    x_c += x_b_off;
                    x_a += x_a_off;
                    z_c += z_b_off;
                    z_a += z_a_off;
                    grad_c += grad_b_off;
                    grad_a += grad_a_off;
                    col_c += col_b_off;
                    col_a += col_a_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_b -= y_c;
            y_c -= y_a;
            y_a = lineOffsets[y_a];
            while (--y_c >= 0) {
                drawTexturedScanline(pixels, texture, y_a, x_a >> 16,
                        x_b >> 16, grad_a >> 8, grad_b >> 8, col_a >> 7,
                        col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_a, z_b);
                x_b += x_c_off;
                x_a += x_a_off;
                z_b += z_c_off;
                z_a += z_a_off;
                grad_b += grad_c_off;
                grad_a += grad_a_off;
                col_b += col_c_off;
                col_a += col_a_off;
                y_a += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_b >= 0) {
                drawTexturedScanline(pixels, texture, y_a, x_a >> 16,
                        x_c >> 16, grad_a >> 8, grad_c >> 8, col_a >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_a, z_c);
                x_c += x_b_off;
                x_a += x_a_off;
                z_c += z_b_off;
                z_a += z_a_off;
                grad_c += grad_b_off;
                grad_a += grad_a_off;
                col_c += col_b_off;
                col_a += col_a_off;
                y_a += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        if (y_b <= y_c) {
            if (y_b >= bottomY)
                return;
            if (y_c > bottomY)
                y_c = bottomY;
            if (y_a > bottomY)
                y_a = bottomY;
            if (y_c < y_a) {
                x_a = x_b <<= 16;
                z_a = z_b <<= 16;
                grad_a = grad_b <<= 16;
                col_a = col_b <<= 15;
                if (y_b < 0) {
                    x_a -= x_a_off * y_b;
                    x_b -= x_b_off * y_b;
                    z_a -= z_a_off * y_b;
                    z_b -= z_b_off * y_b;
                    grad_a -= grad_a_off * y_b;
                    grad_b -= grad_b_off * y_b;
                    col_a -= col_a_off * y_b;
                    col_b -= col_b_off * y_b;
                    y_b = 0;
                }
                x_c <<= 16;
                z_c <<= 16;
                grad_c <<= 16;
                col_c <<= 15;
                if (y_c < 0) {
                    x_c -= x_c_off * y_c;
                    z_c -= z_c_off * y_c;
                    grad_c -= grad_c_off * y_c;
                    col_c -= col_c_off * y_c;
                    y_c = 0;
                }
                int i9 = y_b - center_y;
                Oa += Va * i9;
                Ob += Vb * i9;
                Oc += Vc * i9;
                if (y_b != y_c && x_a_off < x_b_off || y_b == y_c
                        && x_a_off > x_c_off) {
                    y_a -= y_c;
                    y_c -= y_b;
                    y_b = lineOffsets[y_b];
                    while (--y_c >= 0) {
                        drawTexturedScanline(pixels, texture, y_b, x_a >> 16,
                                x_b >> 16, grad_a >> 8, grad_b >> 8,
                                col_a >> 7, col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                color, force, floor, z_a, z_b);
                        x_a += x_a_off;
                        x_b += x_b_off;
                        z_a += z_a_off;
                        z_b += z_b_off;
                        grad_a += grad_a_off;
                        grad_b += grad_b_off;
                        col_a += col_a_off;
                        col_b += col_b_off;
                        y_b += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    while (--y_a >= 0) {
                        drawTexturedScanline(pixels, texture, y_b, x_a >> 16,
                                x_c >> 16, grad_a >> 8, grad_c >> 8,
                                col_a >> 7, col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                color, force, floor, z_a, z_c);
                        x_a += x_a_off;
                        x_c += x_c_off;
                        z_a += z_a_off;
                        z_c += z_c_off;
                        grad_a += grad_a_off;
                        grad_c += grad_c_off;
                        col_a += col_a_off;
                        col_c += col_c_off;
                        y_b += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    return;
                }
                y_a -= y_c;
                y_c -= y_b;
                y_b = lineOffsets[y_b];
                while (--y_c >= 0) {
                    drawTexturedScanline(pixels, texture, y_b, x_b >> 16,
                            x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_b, z_a);
                    x_a += x_a_off;
                    x_b += x_b_off;
                    z_a += z_a_off;
                    z_b += z_b_off;
                    grad_a += grad_a_off;
                    grad_b += grad_b_off;
                    col_a += col_a_off;
                    col_b += col_b_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_a >= 0) {
                    drawTexturedScanline(pixels, texture, y_b, x_c >> 16,
                            x_a >> 16, grad_c >> 8, grad_a >> 8, col_c >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_c, z_a);
                    x_a += x_a_off;
                    x_c += x_c_off;
                    z_a += z_a_off;
                    z_c += z_c_off;
                    grad_a += grad_a_off;
                    grad_c += grad_c_off;
                    col_a += col_a_off;
                    col_c += col_c_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            x_c = x_b <<= 16;
            z_c = z_b <<= 16;
            grad_c = grad_b <<= 16;
            col_c = col_b <<= 15;
            if (y_b < 0) {
                x_c -= x_a_off * y_b;
                x_b -= x_b_off * y_b;
                z_c -= z_a_off * y_b;
                z_b -= z_b_off * y_b;
                grad_c -= grad_a_off * y_b;
                grad_b -= grad_b_off * y_b;
                col_c -= col_a_off * y_b;
                col_b -= col_b_off * y_b;
                y_b = 0;
            }
            x_a <<= 16;
            z_a <<= 16;
            grad_a <<= 16;
            col_a <<= 15;
            if (y_a < 0) {
                x_a -= x_c_off * y_a;
                grad_a -= grad_c_off * y_a;
                col_a -= col_c_off * y_a;
                y_a = 0;
            }
            int j9 = y_b - center_y;
            Oa += Va * j9;
            Ob += Vb * j9;
            Oc += Vc * j9;
            if (x_a_off < x_b_off) {
                y_c -= y_a;
                y_a -= y_b;
                y_b = lineOffsets[y_b];
                while (--y_a >= 0) {
                    drawTexturedScanline(pixels, texture, y_b, x_c >> 16,
                            x_b >> 16, grad_c >> 8, grad_b >> 8, col_c >> 7,
                            col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_c, z_b);
                    x_c += x_a_off;
                    x_b += x_b_off;
                    z_c += x_a_off;
                    z_b += x_b_off;
                    grad_c += grad_a_off;
                    grad_b += grad_b_off;
                    col_c += col_a_off;
                    col_b += col_b_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_c >= 0) {
                    drawTexturedScanline(pixels, texture, y_b, x_a >> 16,
                            x_b >> 16, grad_a >> 8, grad_b >> 8, col_a >> 7,
                            col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_a, z_b);
                    x_a += x_c_off;
                    x_b += x_b_off;
                    z_a += z_c_off;
                    z_b += z_b_off;
                    grad_a += grad_c_off;
                    grad_b += grad_b_off;
                    col_a += col_c_off;
                    col_b += col_b_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_c -= y_a;
            y_a -= y_b;
            y_b = lineOffsets[y_b];
            while (--y_a >= 0) {
                drawTexturedScanline(pixels, texture, y_b, x_b >> 16,
                        x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_b, z_c);
                x_c += x_a_off;
                x_b += x_b_off;
                z_c += z_a_off;
                z_b += z_b_off;
                grad_c += grad_a_off;
                grad_b += grad_b_off;
                col_c += col_a_off;
                col_b += col_b_off;
                y_b += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_c >= 0) {
                drawTexturedScanline(pixels, texture, y_b, x_b >> 16,
                        x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                        col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_b, z_a);
                x_a += x_c_off;
                x_b += x_b_off;
                z_a += z_c_off;
                z_b += z_b_off;
                grad_a += grad_c_off;
                grad_b += grad_b_off;
                col_a += col_c_off;
                col_b += col_b_off;
                y_b += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        if (y_c >= bottomY)
            return;
        if (y_a > bottomY)
            y_a = bottomY;
        if (y_b > bottomY)
            y_b = bottomY;
        if (y_a < y_b) {
            x_b = x_c <<= 16;
            z_b = z_c <<= 16;
            grad_b = grad_c <<= 16;
            col_b = col_c <<= 15;
            if (y_c < 0) {
                x_b -= x_b_off * y_c;
                x_c -= x_c_off * y_c;
                z_b -= z_b_off * y_c;
                z_c -= z_c_off * y_c;
                grad_b -= grad_b_off * y_c;
                grad_c -= grad_c_off * y_c;
                col_b -= col_b_off * y_c;
                col_c -= col_c_off * y_c;
                y_c = 0;
            }
            x_a <<= 16;
            z_a <<= 16;
            grad_a <<= 16;
            col_a <<= 15;
            if (y_a < 0) {
                x_a -= x_a_off * y_a;
                z_a -= z_a_off * y_a;
                grad_a -= grad_a_off * y_a;
                col_a -= col_a_off * y_a;
                y_a = 0;
            }
            int k9 = y_c - center_y;
            Oa += Va * k9;
            Ob += Vb * k9;
            Oc += Vc * k9;
            if (x_b_off < x_c_off) {
                y_b -= y_a;
                y_a -= y_c;
                y_c = lineOffsets[y_c];
                while (--y_a >= 0) {
                    drawTexturedScanline(pixels, texture, y_c, x_b >> 16,
                            x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                            col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_b, z_c);
                    x_b += x_b_off;
                    x_c += x_c_off;
                    z_b += z_b_off;
                    z_c += z_c_off;
                    grad_b += grad_b_off;
                    grad_c += grad_c_off;
                    col_b += col_b_off;
                    col_c += col_c_off;
                    y_c += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_b >= 0) {
                    drawTexturedScanline(pixels, texture, y_c, x_b >> 16,
                            x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force,
                            floor, z_b, z_a);
                    x_b += x_b_off;
                    x_a += x_a_off;
                    z_b += z_b_off;
                    z_a += z_a_off;
                    grad_b += grad_b_off;
                    grad_a += grad_a_off;
                    col_b += col_b_off;
                    col_a += col_a_off;
                    y_c += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_b -= y_a;
            y_a -= y_c;
            y_c = lineOffsets[y_c];
            while (--y_a >= 0) {
                drawTexturedScanline(pixels, texture, y_c, x_c >> 16,
                        x_b >> 16, grad_c >> 8, grad_b >> 8, col_c >> 7,
                        col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_c, z_b);
                x_b += x_b_off;
                x_c += x_c_off;
                z_b += z_b_off;
                z_c += z_c_off;
                grad_b += grad_b_off;
                grad_c += grad_c_off;
                col_b += col_b_off;
                col_c += col_c_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_b >= 0) {
                drawTexturedScanline(pixels, texture, y_c, x_a >> 16,
                        x_b >> 16, grad_a >> 8, grad_b >> 8, col_a >> 7,
                        col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_a, z_b);
                x_b += x_b_off;
                x_a += x_a_off;
                z_b += z_b_off;
                z_a += z_a_off;
                grad_b += grad_b_off;
                grad_a += grad_a_off;
                col_b += col_b_off;
                col_a += col_a_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        x_a = x_c <<= 16;
        z_a = z_c <<= 16;
        grad_a = grad_c <<= 16;
        col_a = col_c <<= 15;
        if (y_c < 0) {
            x_a -= x_b_off * y_c;
            x_c -= x_c_off * y_c;
            z_a -= z_b_off * y_c;
            z_c -= z_c_off * y_c;
            grad_a -= grad_b_off * y_c;
            grad_c -= grad_c_off * y_c;
            col_a -= col_b_off * y_c;
            col_c -= col_c_off * y_c;
            y_c = 0;
        }
        x_b <<= 16;
        z_b <<= 16;
        grad_b <<= 16;
        col_b <<= 15;
        if (y_b < 0) {
            x_b -= x_a_off * y_b;
            z_b -= z_a_off * y_b;
            grad_b -= grad_a_off * y_b;
            col_b -= col_a_off * y_b;
            y_b = 0;
        }
        int l9 = y_c - center_y;
        Oa += Va * l9;
        Ob += Vb * l9;
        Oc += Vc * l9;
        if (x_b_off < x_c_off) {
            y_a -= y_b;
            y_b -= y_c;
            y_c = lineOffsets[y_c];
            while (--y_b >= 0) {
                drawTexturedScanline(pixels, texture, y_c, x_a >> 16,
                        x_c >> 16, grad_a >> 8, grad_c >> 8, col_a >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_a, z_c);
                x_a += x_b_off;
                x_c += x_c_off;
                z_a += z_b_off;
                z_c += z_c_off;
                grad_a += grad_b_off;
                grad_c += grad_c_off;
                col_a += col_b_off;
                col_c += col_c_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_a >= 0) {
                drawTexturedScanline(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7, col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_b, z_c);
                x_b += x_a_off;
                x_c += x_c_off;
                z_b += z_a_off;
                z_c += z_c_off;
                grad_b += grad_a_off;
                grad_c += grad_c_off;
                col_b += col_a_off;
                col_c += col_c_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        y_a -= y_b;
        y_b -= y_c;
        y_c = lineOffsets[y_c];
        while (--y_b >= 0) {
            drawTexturedScanline(pixels, texture, y_c, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, col_c >> 7, col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor, z_c, z_a);
            x_a += x_b_off;
            x_c += x_c_off;
            z_a += z_b_off;
            z_c += z_c_off;
            grad_a += grad_b_off;
            grad_c += grad_c_off;
            col_a += col_b_off;
            col_c += col_c_off;
            y_c += width;
            Oa += Va;
            Ob += Vb;
            Oc += Vc;
        }
        while (--y_a >= 0) {
            drawTexturedScanline(pixels, texture, y_c, x_c >> 16, x_b >> 16,
                    grad_c >> 8, grad_b >> 8, col_c >> 7, col_b >> 7, Oa, Ob,
                    Oc, Ha, Hb, Hc, color, force, floor, z_c, z_b);
            x_b += x_a_off;
            x_c += x_c_off;
            z_b += z_a_off;
            z_c += z_c_off;
            grad_b += grad_a_off;
            grad_c += grad_c_off;
            col_b += col_a_off;
            col_c += col_c_off;
            y_c += width;
            Oa += Va;
            Ob += Vb;
            Oc += Vc;
        }
        return;
    }

    private static void drawTexturedScanline(int dest[], int texture[],  int dest_off, int start_x, int end_x, int shadeValue, int gradient, int start_col, int end_col, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int color, boolean force, boolean floor, int z1, int z2) {
        boolean isObject = floor;
        int rgb = 0;
        int loops = 0;
        if (end_x - start_x > 0) {
            z2 = (z2 - z1) / (end_x - start_x);
        }
        if (start_x >= end_x) {
            return;
        }
        int k3;
        int j3;
        if (restrict_edges) {
            j3 = (gradient - shadeValue) / (end_x - start_x);
            if (end_x > DrawingArea.viewportRX) {
                end_x = DrawingArea.viewportRX;
            }
            if (start_x < 0) {
                shadeValue -= start_x * j3;
                z1 -= start_x * z2;
                start_x = 0;
            }
            if (start_x >= end_x) {
                return;
            }
            k3 = end_x - start_x >> 3;
            j3 <<= 12;
            shadeValue <<= 9;
        } else {
            if (end_x - start_x > 7) {
                k3 = end_x - start_x >> 3;
                j3 = (gradient - shadeValue) * shadowDecay[k3] >> 6;
            } else {
                k3 = 0;
                j3 = 0;
            }
            shadeValue <<= 9;
        }
        end_col = (end_col - start_col) * shadowDecay[end_x - start_x >> 2] >> 14;
        dest_off += start_x;
        int j4 = 0;
        int l4 = 0;
        int l6 = start_x - center_x;
        arg7 += (arg10 >> 3) * l6;
        arg8 += (arg11 >> 3) * l6;
        arg9 += (arg12 >> 3) * l6;
        int l5 = arg9 >> 14;
        if (l5 != 0) {
            rgb = arg7 / l5;
            loops = arg8 / l5;
            if (rgb < 0) {
                rgb = 0;
            } else if (rgb > 16256) {
                rgb = 16256;
            }
        }
        arg7 += arg10;
        arg8 += arg11;
        arg9 += arg12;
        l5 = arg9 >> 14;
        if (l5 != 0) {
            j4 = arg7 / l5;
            l4 = arg8 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int j7 = j4 - rgb >> 3;
        int l7 = l4 - loops >> 3;
        rgb += shadeValue & 0x600000;
        int glb_alpha = alpha;
        if (glb_alpha < 0 || glb_alpha > 0xff) {
            glb_alpha = 0;
		}
        glb_alpha = 0xff - glb_alpha;
        int src;
        int src_alpha;
        int src_delta;
        int dst;
        while (k3-- > 0) {
            for (int i = 0; i != 8; ++i) {
                src = texture[(loops & 0x3f80) + (rgb >> 7)];
                src_alpha = src >>> 24;
                if (src_alpha != 0 || force) {
                    if (src_alpha != 0xff && color >= 0) {
                        if (src_alpha == 0)
                            src = color;
                        else {
                            src_delta = 0xff - src_alpha;
                            src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
                        }
                        src_alpha = 0xff;
                    }
                    if (glb_alpha != 0xff) {
                        src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;
					}
                    if (src_alpha != 0) {
                        if (src_alpha == 0xff) {
                            if (saveDepth) {
                                depthBuffer[dest_off] = z1;
                                z1 += z2;
                            }
                            dest[dest_off] = (src & 0xffffff);
                        } else {
                            int dest_alpha = 0xff - src_alpha;
                            if (saveDepth) {
                                depthBuffer[dest_off] = (z1 >> 8) * dest_alpha + (depthBuffer[dest_off] >> 8) * src_alpha;
                                z1 += z2;
                            }
                            dst = dest[dest_off];
                            src_delta = 0xff - src_alpha;
                            dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8));
                        }
                    }
                } else {
                	
                }
                if (!isObject) {
                    int tex = texture[(loops & 0x3f80) + (rgb >> 7)];
                    dest[dest_off] = hsl2rgb[(start_col >> 8 & 0xff80) | ((start_col >> 8 & 0x7f) * ((tex >> 17 & 0x7f) + (tex >> 9 & 0x7f) + (tex >> 1 & 0x7f) + 0x7f >> 2) >> 7)];
                } else {
                    dst = dest[dest_off];
                    src_delta = 0xff - src_alpha;
                    dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8));
                }
                dest_off++;
                rgb += j7;
                loops += l7;
            }
            start_col += end_col;
            rgb = j4;
            loops = l4;
            arg7 += arg10;
            arg8 += arg11;
            arg9 += arg12;
            int i6 = arg9 >> 14;
            if (i6 != 0) {
                j4 = arg7 / i6;
                l4 = arg8 / i6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            j7 = j4 - rgb >> 3;
            l7 = l4 - loops >> 3;
            shadeValue += j3;
            rgb += shadeValue & 0x600000;
        }
        for (k3 = end_x - start_x & 7; k3-- > 0; ) {
            src = texture[(loops & 0x3f80) + (rgb >> 7)];
            src_alpha = src >>> 24;
            if (src_alpha != 0) {
                if (src_alpha != 0xff && color >= 0) {
                    if (src_alpha == 0)
                        src = color;
                    else {
                        src_delta = 0xff - src_alpha;
                        src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
                    }
                    src_alpha = 0xff;
                }
                if (glb_alpha != 0xff) {
                    src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;
				}
                if (src_alpha != 0) {
                    if (src_alpha == 0xff) {
                        if (saveDepth) {
                            depthBuffer[dest_off] = z1;
                            z1 += z2;
                        }
                        dest[dest_off] = src & 0xffffff;
                    } else {
                        int dest_alpha = 0xff - src_alpha;
                        if (saveDepth) {
                            depthBuffer[dest_off] = (z1 >> 8) * dest_alpha + (depthBuffer[dest_off] >> 8) * src_alpha;
                            z1 += z2;
                        }
                        dst = dest[dest_off];
                        src_delta = 0xff - src_alpha;
                        dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8));
                    }
                }
            }
            if (!isObject) {
                int tex = texture[(loops & 0x3f80) + (rgb >> 7)];
                dest[dest_off] = hsl2rgb[(start_col >> 8 & 0xff80) | ((start_col >> 8 & 0x7f) * ((tex >> 17 & 0x7f) + (tex >> 9 & 0x7f) + (tex >> 1 & 0x7f) + 0x7f >> 2) >> 7)];
            } else {
                dst = dest[dest_off];
                src_delta = 0xff - src_alpha;
                dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8));
            }
            dest_off++;
            rgb += j7;
            loops += l7;
        }
    }

	public static boolean drawDepthTexturedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int z1, int z2, int z3, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int Mz, int Nz, int Py, int My, int Ny, int t_id, int color, boolean force) {
		brighten = t_id == 24;
		if (saveDepth) {
			drawDepthFlatTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z1, z2, z3, 0);
		}
		return drawTexturedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, Mz, Nz, Py, My, Ny, t_id, color, force);
	}

	public static boolean drawTexturedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int Mz, int Nz, int Py, int My, int Ny, int t_id, int color, boolean force) {
		if (t_id < 0 || t_id >= TextureDef.textures.length) {
			drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
			return true;
		}
		TextureDef def = TextureDef.textures[t_id];
		if (def == null || (!force && !def.aBoolean1223 && lowMem)) {
			drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
			return true;
		}
		int texture[] = null;
		if(Client.getOption("hd_tex"))
			texture = TextureLoader667.getTexturePixels(t_id);
		else
			texture = TextureLoader317.getTexturePixels(t_id);
		if (texture == null) {
			drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
			return false;
		}
		if (color >= 0xffff) {
			color = -1;
		}

		if (color >= 0) {
			color = hsl2rgb[color];
		}

		++triangles;
		Mx = Px - Mx;
		Mz = Pz - Mz;
		My = Py - My;
		Nx -= Px;
		Nz -= Pz;
		Ny -= Py;
		int Oa = (Nx * Pz - Nz * Px) * WorldController.focalLength << 5;
		int Ha = Nz * Py - Ny * Pz << 8;
		int Va = Ny * Px - Nx * Py << 5;
		int Ob = (Mx * Pz - Mz * Px) * WorldController.focalLength << 5;
		int Hb = Mz * Py - My * Pz << 8;
		int Vb = My * Px - Mx * Py << 5;
		int Oc = (Mz * Nx - Mx * Nz) * WorldController.focalLength << 5;
		int Hc = My * Nz - Mz * Ny << 8;
		int Vc = Mx * Ny - My * Nx << 5;
		int x_a_off = 0;
		int grad_a_off = 0;
		if (y_b != y_a) {
			x_a_off = (x_b - x_a << 16) / (y_b - y_a);
			grad_a_off = (grad_b - grad_a << 16) / (y_b - y_a);
		}
		int x_b_off = 0;
		int grad_b_off = 0;
		if (y_c != y_b) {
			x_b_off = (x_c - x_b << 16) / (y_c - y_b);
			grad_b_off = (grad_c - grad_b << 16) / (y_c - y_b);
		}
		int x_c_off = 0;
		int grad_c_off = 0;
		if (y_c != y_a) {
			x_c_off = (x_a - x_c << 16) / (y_a - y_c);
			grad_c_off = (grad_a - grad_c << 16) / (y_a - y_c);
		}
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= DrawingArea.bottomY) {
				return true;
			}
			if (y_b > DrawingArea.bottomY) {
				y_b = DrawingArea.bottomY;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				grad_c = grad_a <<= 16;
				if (y_a < 0) {
					x_c -= x_c_off * y_a;
					x_a -= x_a_off * y_a;
					grad_c -= grad_c_off * y_a;
					grad_a -= grad_a_off * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				grad_b <<= 16;
				if (y_b < 0) {
					x_b -= x_b_off * y_b;
					grad_b -= grad_b_off * y_b;
					y_b = 0;
				}
				int jA = y_a - center_y;
				Oa += Va * jA;
				Ob += Vb * jA;
				Oc += Vc * jA;
				y_c -= y_b;
				y_b -= y_a;
				y_a = lineOffsets[y_a];
				while (--y_b >= 0) {
					drawTexturedLine(pixels, texture, y_a, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_c_off;
					x_a += x_a_off;
					grad_c += grad_c_off;
					grad_a += grad_a_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_c >= 0) {
					drawTexturedLine(pixels, texture, y_a, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_c_off;
					x_b += x_b_off;
					grad_c += grad_c_off;
					grad_b += grad_b_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			x_b = x_a <<= 16;
			grad_b = grad_a <<= 16;
			if (y_a < 0) {
				x_b -= x_c_off * y_a;
				x_a -= x_a_off * y_a;
				grad_b -= grad_c_off * y_a;
				grad_a -= grad_a_off * y_a;
				y_a = 0;
			}
			x_c <<= 16;
			grad_c <<= 16;
			if (y_c < 0) {
				x_c -= x_b_off * y_c;
				grad_c -= grad_b_off * y_c;
				y_c = 0;
			}
			int l8 = y_a - center_y;
			Oa += Va * l8;
			Ob += Vb * l8;
			Oc += Vc * l8;
			/*if (y_a != y_c && x_c_off < x_a_off || y_a == y_c && x_b_off > x_a_off) {
					y_b -= y_c;
					y_c -= y_a;
					y_a = lineOffsets[y_a];
					while (--y_c >= 0) {
						drawTexturedLine(pixels, texture, y_a, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_b += x_c_off;
						x_a += x_a_off;
						grad_b += grad_c_off;
						grad_a += grad_a_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_b >= 0) {
						drawTexturedLine(pixels, texture, y_a, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_b_off;
						x_a += x_a_off;
						grad_c += grad_b_off;
						grad_a += grad_a_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
			y_b -= y_c;
			y_c -= y_a;
			y_a = lineOffsets[y_a];
			while (--y_c >= 0) {
				drawTexturedLine(pixels, texture, y_a, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_c_off;
				x_a += x_a_off;
				grad_b += grad_c_off;
				grad_a += grad_a_off;
				y_a += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_b >= 0) {
				drawTexturedLine(pixels, texture, y_a, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_c += x_b_off;
				x_a += x_a_off;
				grad_c += grad_b_off;
				grad_a += grad_a_off;
				y_a += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}
		if (y_b <= y_c) {
			if (y_b >= DrawingArea.bottomY) {
				return true;
			}
			if (y_c > DrawingArea.bottomY) {
				y_c = DrawingArea.bottomY;
			}
			if (y_a > DrawingArea.bottomY) {
				y_a = DrawingArea.bottomY;
			}
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				grad_a = grad_b <<= 16;
				if (y_b < 0) {
					x_a -= x_a_off * y_b;
					x_b -= x_b_off * y_b;
					grad_a -= grad_a_off * y_b;
					grad_b -= grad_b_off * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				grad_c <<= 16;
				if (y_c < 0) {
					x_c -= x_c_off * y_c;
					grad_c -= grad_c_off * y_c;
					y_c = 0;
				}
				int i9 = y_b - center_y;
				Oa += Va * i9;
				Ob += Vb * i9;
				Oc += Vc * i9;
				/*if (y_b != y_c && x_a_off < x_b_off || y_b == y_c && x_a_off > x_c_off) {
						y_a -= y_c;
						y_c -= y_b;
						y_b = lineOffsets[y_b];
						while (--y_c >= 0) {
							drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
							x_a += x_a_off;
							x_b += x_b_off;
							grad_a += grad_a_off;
							grad_b += grad_b_off;
							y_b += width;
							Oa += Va;
							Ob += Vb;
							Oc += Vc;
						}
						while (--y_a >= 0) {
							drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
							x_a += x_a_off;
							x_c += x_c_off;
							grad_a += grad_a_off;
							grad_c += grad_c_off;
							y_b += width;
							Oa += Va;
							Ob += Vb;
							Oc += Vc;
						}
						return true;
					}*/
				y_a -= y_c;
				y_c -= y_b;
				y_b = lineOffsets[y_b];
				//not these
				while (--y_c >= 0) {
					drawTexturedLine(pixels, texture, y_b, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_a_off;
					x_b += x_b_off;
					grad_a += grad_a_off;
					grad_b += grad_b_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_a >= 0) {
					drawTexturedLine(pixels, texture, y_b, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_a_off;
					x_c += x_c_off;
					grad_a += grad_a_off;
					grad_c += grad_c_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			x_c = x_b <<= 16;
			grad_c = grad_b <<= 16;
			if (y_b < 0) {
				x_c -= x_a_off * y_b;
				x_b -= x_b_off * y_b;
				grad_c -= grad_a_off * y_b;
				grad_b -= grad_b_off * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			grad_a <<= 16;
			if (y_a < 0) {
				x_a -= x_c_off * y_a;
				grad_a -= grad_c_off * y_a;
				y_a = 0;
			}
			int j9 = y_b - center_y;
			Oa += Va * j9;
			Ob += Vb * j9;
			Oc += Vc * j9;
			/*if (x_a_off < x_b_off) {
					y_c -= y_a;
					y_a -= y_b;
					y_b = lineOffsets[y_b];
					while (--y_a >= 0) {
						drawTexturedLine(pixels, texture, y_b, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_a_off;
						x_b += x_b_off;
						grad_c += grad_a_off;
						grad_b += grad_b_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_c >= 0) {
						drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_a += x_c_off;
						x_b += x_b_off;
						grad_a += grad_c_off;
						grad_b += grad_b_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
			y_c -= y_a;
			y_a -= y_b;
			y_b = lineOffsets[y_b];
			//not these
			while (--y_a >= 0) {
				drawTexturedLine(pixels, texture, y_b, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_c += x_a_off;
				x_b += x_b_off;
				grad_c += grad_a_off;
				grad_b += grad_b_off;
				y_b += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_c >= 0) {
				drawTexturedLine(pixels, texture, y_b, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_a += x_c_off;
				x_b += x_b_off;
				grad_a += grad_c_off;
				grad_b += grad_b_off;
				y_b += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}
		if (y_c >= DrawingArea.bottomY) {
			return true;
		}
		if (y_a > DrawingArea.bottomY) {
			y_a = DrawingArea.bottomY;
		}
		if (y_b > DrawingArea.bottomY) {
			y_b = DrawingArea.bottomY;
		}
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			grad_b = grad_c <<= 16;
			if (y_c < 0) {
				x_b -= x_b_off * y_c;
				x_c -= x_c_off * y_c;
				grad_b -= grad_b_off * y_c;
				grad_c -= grad_c_off * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			grad_a <<= 16;
			if (y_a < 0) {
				x_a -= x_a_off * y_a;
				grad_a -= grad_a_off * y_a;
				y_a = 0;
			}
			int k9 = y_c - center_y;
			Oa += Va * k9;
			Ob += Vb * k9;
			Oc += Vc * k9;
			/*if (x_b_off < x_c_off) {
					y_b -= y_a;
					y_a -= y_c;
					y_c = lineOffsets[y_c];
					while (--y_a >= 0) {
						drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_b += x_b_off;
						x_c += x_c_off;
						grad_b += grad_b_off;
						grad_c += grad_c_off;
						y_c += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_b >= 0) {
						drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_b += x_b_off;
						x_a += x_a_off;
						grad_b += grad_b_off;
						grad_a += grad_a_off;
						y_c += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
			y_b -= y_a;
			y_a -= y_c;
			y_c = lineOffsets[y_c];
			//not these
			while (--y_a >= 0) {
				drawTexturedLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_b_off;
				x_c += x_c_off;
				grad_b += grad_b_off;
				grad_c += grad_c_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_b >= 0) {
				drawTexturedLine(pixels, texture, y_c, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_b_off;
				x_a += x_a_off;
				grad_b += grad_b_off;
				grad_a += grad_a_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}
		x_a = x_c <<= 16;
		grad_a = grad_c <<= 16;
		if (y_c < 0) {
			x_a -= x_b_off * y_c;
			x_c -= x_c_off * y_c;
			grad_a -= grad_b_off * y_c;
			grad_c -= grad_c_off * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		grad_b <<= 16;
		if (y_b < 0) {
			x_b -= x_a_off * y_b;
			grad_b -= grad_a_off * y_b;
			y_b = 0;
		}
		int l9 = y_c - center_y;
		Oa += Va * l9;
		Ob += Vb * l9;
		Oc += Vc * l9;
		if (x_b_off < x_c_off) {
			y_a -= y_b;
			y_b -= y_c;
			y_c = lineOffsets[y_c];
			/*while (--y_b >= 0) {
					drawTexturedLine(pixels, texture, y_c, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_b_off;
					x_c += x_c_off;
					grad_a += grad_b_off;
					grad_c += grad_c_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_a >= 0) {
					drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_a_off;
					x_c += x_c_off;
					grad_b += grad_a_off;
					grad_c += grad_c_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}*/
			return true;
		}
		y_a -= y_b;
		y_b -= y_c;
		y_c = lineOffsets[y_c];
		//not these
		while (--y_b >= 0) {
			drawTexturedLine(pixels, texture, y_c, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
			x_a += x_b_off;
			x_c += x_c_off;
			grad_a += grad_b_off;
			grad_c += grad_c_off;
			y_c += width;
			Oa += Va;
			Ob += Vb;
			Oc += Vc;
		}
		while (--y_a >= 0) {
			drawTexturedLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
			x_b += x_a_off;
			x_c += x_c_off;
			grad_b += grad_a_off;
			grad_c += grad_c_off;
			y_c += width;
			Oa += Va;
			Ob += Vb;
			Oc += Vc;
		}
		return true;
	}

	private static void drawTexturedLine(int dest[], int texture[], int dest_off, int start_x, int end_x, int shadeValue, int gradient, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int color, boolean force) {
		//shadeValue = 500;//lol makes textures ultra bright and makes triangles visible - slightly wrong name.. meh
		int rgb = 0;
		int loops = 0;
		if (start_x >= end_x) {
			return;
		}
		int j3;
		int k3;
		if (restrict_edges) {
			j3 = (gradient - shadeValue) / (end_x - start_x);
			if (end_x > DrawingArea.viewportRX) {
				end_x = DrawingArea.viewportRX;
			}
			if (start_x < 0) {
				shadeValue -= start_x * j3;
				start_x = 0;
			}
			if (start_x >= end_x) {
				return;
			}
			k3 = end_x - start_x >> 3;
			j3 <<= 12;
			shadeValue <<= 9;
		} else {
			if (end_x - start_x > 7) {
				k3 = end_x - start_x >> 3;
			j3 = (gradient - shadeValue) * shadowDecay[k3] >> 6;
			} else {
				k3 = 0;
				j3 = 0;
			}
			shadeValue <<= 9;
		}
		dest_off += start_x;
		int j4 = 0;
		int l4 = 0;
		int l6 = start_x - center_x;
		arg7 += (arg10 >> 3) * l6;
		arg8 += (arg11 >> 3) * l6;
		arg9 += (arg12 >> 3) * l6;
		int l5 = arg9 >> 14;
		if (l5 != 0) {
			rgb = arg7 / l5;
			loops = arg8 / l5;
			if (rgb < 0) {
				rgb = 0;
			} else if (rgb > 16256) {
				rgb = 16256;
			}
		}
		arg7 += arg10;
		arg8 += arg11;
		arg9 += arg12;
		l5 = arg9 >> 14;
		if (l5 != 0) {
			j4 = arg7 / l5;
			l4 = arg8 / l5;
			if (j4 < 7) {
				j4 = 7;
			} else if (j4 > 16256) {
				j4 = 16256;
			}
		}
		int j7 = j4 - rgb >> 3;
		int l7 = l4 - loops >> 3;
		rgb += shadeValue & 0x600000;
		int glb_alpha = alpha;
		if (glb_alpha < 0 || glb_alpha >= 0xff)
			glb_alpha = 0;

		int src;
		int src_alpha;
		int src_delta;
		int dst;
		while (k3-- > 0)
		{
			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;

			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			//rgb += j7;
			//loops += l7;

			rgb = j4;
			loops = l4;
			arg7 += arg10;
			arg8 += arg11;
			arg9 += arg12;
			int i6 = arg9 >> 14;
			if (i6 != 0) {
				j4 = arg7 / i6;
				l4 = arg8 / i6;
				if (j4 < 7) {
					j4 = 7;
				} else if (j4 > 16256) {
					j4 = 16256;
				}
			}
			j7 = j4 - rgb >> 3;
			l7 = l4 - loops >> 3;
			shadeValue += j3;
			rgb += shadeValue & 0x600000;
		}
		for (k3 = end_x - start_x & 7; k3-- > 0; ) {
			src = texture[(loops & 0x3f80) + (rgb >> 7)];
			src_alpha = src >>> 24;
			if (src_alpha != 0 || force)
			{
				if (src_alpha != 0xff && color >= 0)
				{
					if (src_alpha == 0)
						src = color;

					else
					{
						src_delta = 0xff - src_alpha;
						src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
					}
					src_alpha = 0xff;
				}
				if (glb_alpha > 0)
					src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = brighten(src & 0xffffff);

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = brighten((((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff);
						}
					}
			}
			dest_off++;
			rgb += j7;
			loops += l7;
		}

	}
}