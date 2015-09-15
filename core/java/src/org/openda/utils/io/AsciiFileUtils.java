/* MOD_V2.0
 * Copyright (c) 2012 OpenDA Association
 * All rights reserved.
 *
 * This file is part of OpenDA.
 *
 * OpenDA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * OpenDA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenDA.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openda.utils.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsciiFileUtils {
	/**
	 * Reads the contents of the given ascii file.
	 *
	 * @param inputFile file to read
	 * @return each line is put in a separate String
	 */
	public static List<String> readLines(File inputFile) {
		List<String> lines = new ArrayList<String>();

		try {
			BufferedReader input = new BufferedReader(new FileReader(inputFile));
			try {
				String line = input.readLine();
				while (line != null) {
					lines.add(line);
					line = input.readLine();
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException e){
			throw new RuntimeException("Problem while opening file " + inputFile.getAbsolutePath() + " Message was " + e.getMessage(), e);
		} catch (IOException e){
			throw new RuntimeException("Problem while reading file " + inputFile.getAbsolutePath() + " Message was " + e.getMessage(), e);
		}

		return lines;
	}

	/**
	 * Writes the given list with Strings to the given ascii file.
	 *
	 * @param outputFile file to write to
	 * @param lines content to write, each String is written on a separate line
	 */
	public static void writeLines(File outputFile, List<String> lines) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			try {
				for (String line : lines) {
					writer.write(line);
					writer.newLine();
				}
			} finally {
				writer.close();
			}
		} catch (IOException e){
			throw new RuntimeException("Problem while writing file " + outputFile.getAbsolutePath() + " Message was " + e.getMessage(), e);
		}
	}
}
