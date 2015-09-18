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

import org.openda.exchange.*;
import org.openda.interfaces.*;

import java.io.File;
import java.util.*;

/**
 * Class for writing data from model state exchange items before and after analysis (state update) to netcdf files.
 * This can be easily used e.g. in any ModelInstance implementation.
 *
 * @author Arno Kockx
 */
//TODO move AnalysisOutputXML to core xsd
public class AnalysisDataWriter {
	private final NetcdfGridExchangeItemWriter beforeAnalysisScalarWriter;
	private final NetcdfGridExchangeItemWriter beforeAnalysisGridWriter;

	private final NetcdfGridExchangeItemWriter afterAnalysisScalarWriter;
	private final NetcdfGridExchangeItemWriter afterAnalysisGridWriter;

	/**
	 * @param exchangeItems with data to write.
	 * @param outputFolder in which output files will be written.
	 */
	public AnalysisDataWriter(Collection<IExchangeItem> exchangeItems, File outputFolder) {
		if (exchangeItems == null) throw new IllegalArgumentException("exchangeItems == null");
		if (exchangeItems.isEmpty()) throw new IllegalArgumentException("exchangeItems.isEmpty()");

		List<IExchangeItem> scalarItems = new ArrayList<>();
		List<IExchangeItem> gridItems = new ArrayList<>();
		for (IExchangeItem item : exchangeItems) {
			IGeometryInfo geometryInfo = item.getGeometryInfo();
			if (geometryInfo == null || geometryInfo instanceof PointGeometryInfo) {//if scalar.
				scalarItems.add(item);
			} else if (geometryInfo instanceof IrregularGridGeometryInfo || geometryInfo instanceof ArrayGeometryInfo) {//if grid.
				gridItems.add(item);
			} else {
				throw new UnsupportedOperationException(getClass().getSimpleName() + ": exchangeItem '" + item.getId() + "' of type " + item.getClass().getSimpleName()
						+ " has an unknown geometryInfo type: " + geometryInfo.getClass().getSimpleName());
			}
		}

		//TODO also write scalars
//		if (scalarItems.isEmpty()) {
			beforeAnalysisScalarWriter = null;
			afterAnalysisScalarWriter = null;
//		} else {
//			beforeAnalysisScalarWriter = createScalarWriter(scalarItems, outputFolder, "model_scalar_data_before_analysis.nc");
//			afterAnalysisScalarWriter = createScalarWriter(scalarItems, outputFolder, "model_scalar_data_after_analysis.nc");
//		}

		if (gridItems.isEmpty()) {
			beforeAnalysisGridWriter = null;
			afterAnalysisGridWriter = null;
		} else {
			beforeAnalysisGridWriter = createGridWriter(gridItems, outputFolder, "model_grid_data_before_analysis.nc");
			afterAnalysisGridWriter = createGridWriter(gridItems, outputFolder, "model_grid_data_after_analysis.nc");
		}
	}

//	private NetcdfScalarExchangeItemWriter createScalarWriter(List<IExchangeItem> scalarItems, File outputFolder, String outputFileName) {
//		File outputFile = new File(outputFolder, outputFileName);
//		return new NetcdfScalarExchangeItemWriter(scalarItems.toArray(new IExchangeItem[scalarItems.size()]), outputFile);
//	}

	private NetcdfGridExchangeItemWriter createGridWriter(List<IExchangeItem> gridItems, File outputFolder, String outputFileName) {
		File outputFile = new File(outputFolder, outputFileName);
		return new NetcdfGridExchangeItemWriter(gridItems.toArray(new IExchangeItem[gridItems.size()]), outputFile);
	}

	public void writeDataBeforeAnalysis() {
		if (beforeAnalysisScalarWriter != null) beforeAnalysisScalarWriter.writeDataForCurrentTimeStep();
		if (beforeAnalysisGridWriter != null) beforeAnalysisGridWriter.writeDataForCurrentTimeStep();
	}

	public void writeDataAfterAnalysis() {
		if (afterAnalysisScalarWriter != null) afterAnalysisScalarWriter.writeDataForCurrentTimeStep();
		if (afterAnalysisGridWriter != null) afterAnalysisGridWriter.writeDataForCurrentTimeStep();
	}

	public void close() {
		if (beforeAnalysisGridWriter != null) beforeAnalysisGridWriter.close();
		if (afterAnalysisGridWriter != null) afterAnalysisGridWriter.close();
	}
}
