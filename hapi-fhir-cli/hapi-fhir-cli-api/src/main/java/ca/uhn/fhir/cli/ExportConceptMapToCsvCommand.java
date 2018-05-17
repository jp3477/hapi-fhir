package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class ExportConceptMapToCsvCommand extends AbstractImportExportCsvConceptMapCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExportConceptMapToCsvCommand.class);

	@Override
	public String getCommandDescription() {
		return "Exports a specific ConceptMap resource to a CSV file.";
	}

	@Override
	public String getCommandName() {
		return "export-conceptmap-to-csv";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();

		this.addFhirVersionOption(options);
		addBaseUrlOption(options);
		addRequiredOption(options, CONCEPTMAP_URL_PARAM, CONCEPTMAP_URL_PARAM_LONGOPT, CONCEPTMAP_URL_PARAM_NAME, CONCEPTMAP_URL_PARAM_DESC);
		addRequiredOption(options, FILE_PARAM, FILE_PARAM_LONGOPT, FILE_PARAM_NAME, FILE_PARAM_DESC);
		addBasicAuthOption(options);
		addVerboseLoggingOption(options);

		return options;
	}

	@Override
	protected void process() throws ParseException {
		searchForConceptMapByUrl();
	}

	private void searchForConceptMapByUrl() {
		ourLog.info("Searching for ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		if (fhirVersion == FhirVersionEnum.DSTU3) {
			org.hl7.fhir.dstu3.model.Bundle response = client
				.search()
				.forResource(org.hl7.fhir.dstu3.model.ConceptMap.class)
				.where(org.hl7.fhir.dstu3.model.ConceptMap.URL.matches().value(conceptMapUrl))
				.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
				.execute();

			if (response.hasEntry()) {
				ourLog.info("Found ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
				org.hl7.fhir.dstu3.model.ConceptMap conceptMap = (org.hl7.fhir.dstu3.model.ConceptMap) response.getEntryFirstRep().getResource();
				convertConceptMapToCsv(conceptMap);
			} else {
				ourLog.info("No ConceptMap exists with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
			}
		} else if (fhirVersion == FhirVersionEnum.R4) {
			Bundle response = client
				.search()
				.forResource(ConceptMap.class)
				.where(ConceptMap.URL.matches().value(conceptMapUrl))
				.returnBundle(Bundle.class)
				.execute();

			if (response.hasEntry()) {
				ourLog.info("Found ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
				ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();
				convertConceptMapToCsv(conceptMap);
			} else {
				ourLog.info("No ConceptMap exists with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
			}
		}
	}

	private void convertConceptMapToCsv(org.hl7.fhir.dstu3.model.ConceptMap theConceptMap) {
		ourLog.info("Exporting ConceptMap to CSV...");
		BufferedWriter bufferedWriter = null;
		CSVPrinter csvPrinter = null;
		try {
			bufferedWriter = Files.newBufferedWriter(Paths.get(file));
			csvPrinter = new CSVPrinter(
				bufferedWriter,
				CSVFormat
					.DEFAULT
					.withRecordSeparator("\n")
					.withHeader(Header.class));

			for (org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {
				for (org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent element : group.getElement()) {
					for (org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent target : element.getTarget()) {

						List<String> columns = new ArrayList<>();
						columns.add(defaultString(group.getSource()));
						columns.add(defaultString(group.getSourceVersion()));
						columns.add(defaultString(group.getTarget()));
						columns.add(defaultString(group.getTargetVersion()));
						columns.add(defaultString(element.getCode()));
						columns.add(defaultString(element.getDisplay()));
						columns.add(defaultString(target.getCode()));
						columns.add(defaultString(target.getDisplay()));
						columns.add(defaultString(target.getEquivalence().toCode()));
						columns.add(defaultString(target.getComment()));

						csvPrinter.print(columns);
					}
				}
			}
		} catch (IOException ioe) {
			throw new InternalErrorException(ioe);
		} finally {
			IOUtils.closeQuietly(csvPrinter);
			IOUtils.closeQuietly(bufferedWriter);
		}
		ourLog.info("Finished exporting to {}", file);
	}

	private void convertConceptMapToCsv(ConceptMap theConceptMap) {
		ourLog.info("Exporting ConceptMap to CSV...");
		Writer writer = null;
		CSVPrinter csvPrinter = null;
		try {
			writer = Files.newBufferedWriter(Paths.get(file));
			csvPrinter = new CSVPrinter(
				writer,
				CSVFormat
					.DEFAULT
					.withRecordSeparator("\n")
					.withHeader(Header.class));

			for (ConceptMapGroupComponent group : theConceptMap.getGroup()) {
				for (SourceElementComponent element : group.getElement()) {
					for (ConceptMap.TargetElementComponent target : element.getTarget()) {

						List<String> columns = new ArrayList<>();
						columns.add(defaultString(group.getSource()));
						columns.add(defaultString(group.getSourceVersion()));
						columns.add(defaultString(group.getTarget()));
						columns.add(defaultString(group.getTargetVersion()));
						columns.add(defaultString(element.getCode()));
						columns.add(defaultString(element.getDisplay()));
						columns.add(defaultString(target.getCode()));
						columns.add(defaultString(target.getDisplay()));
						columns.add(defaultString(target.getEquivalence().toCode()));
						columns.add(defaultString(target.getComment()));

						csvPrinter.printRecord(columns);
					}
				}
			}
		} catch (IOException ioe) {
			throw new InternalErrorException(ioe);
		} finally {
			IOUtils.closeQuietly(csvPrinter);
			IOUtils.closeQuietly(writer);
		}
		ourLog.info("Finished exporting to {}", file);
	}
}
