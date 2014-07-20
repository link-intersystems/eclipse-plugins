package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

public class CronExpressionProposalProvider implements IContentProposalProvider {

	private List<IContentProposal> proposals;

	public CronExpressionProposalProvider() {
		proposals = new ArrayList<IContentProposal>();
		proposals.add(new CronContentProposal("0 0 12 * * ?",
				"Fire at 12pm (noon) every day"));

		proposals.add(new CronContentProposal("0 15 10 ? * *",
				"Fire at 10:15am every day"));
		proposals.add(new CronContentProposal("0 15 10 * * ?",
				"Fire at 10:15am every day"));
		proposals.add(new CronContentProposal("0 15 10 * * ? *",
				"Fire at 10:15am every day"));
		proposals.add(new ContentProposal("0 15 10 * * ? 2005",
				"Fire at 10:15am every day during the year 2005"));
		proposals
				.add(new CronContentProposal("0 * 14 * * ?",
						"Fire every minute starting at 2pm and ending at 2:59pm, every day"));
		proposals
				.add(new CronContentProposal("0 0/5 14 * * ?",
						"Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day"));
		proposals
				.add(new CronContentProposal(
						"0 0/5 14,18 * * ?",
						"Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm, every day"));
		proposals
				.add(new CronContentProposal("0 0-5 14 * * ?",
						"Fire every minute starting at 2pm and ending at 2:05pm, every day"));
		proposals
				.add(new CronContentProposal("0 10,44 14 ? 3 WED",
						"Fire at 2:10pm and at 2:44pm every Wednesday in the month of March."));
		proposals
				.add(new CronContentProposal("0 15 10 ? * MON-FRI",
						"Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday"));
		proposals.add(new CronContentProposal("0 15 10 15 * ?",
				"Fire at 10:15am on the 15th day of every month"));
		proposals.add(new CronContentProposal("0 15 10 L * ?",
				"Fire at 10:15am on the last day of every month"));
		proposals.add(new CronContentProposal("0 15 10 ? * 6L",
				"Fire at 10:15am on the last Friday of every month"));
		proposals.add(new CronContentProposal("0 15 10 ? * 6L",
				"Fire at 10:15am on the last Friday of every month"));
		proposals
				.add(new CronContentProposal(
						"0 15 10 ? * 6L 2002-2005",
						"Fire at 10:15am on every last friday of every month during the years 2002, 2003, 2004 and 2005"));
		proposals.add(new CronContentProposal("0 15 10 ? * 6#3",
				"Fire at 10:15am on the third Friday of every month"));
		proposals
				.add(new CronContentProposal(
						"0 0 12 1/5 * ?",
						"Fire at 12pm (noon) every 5 days every month, starting on the first day of the month."));
		proposals.add(new CronContentProposal("0 11 11 11 11 ?",
				"Fire every November 11th at 11:11am."));
	}

	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> matchingProposals = new ArrayList<IContentProposal>();
		contents = contents.trim();
		for (IContentProposal contentProposal : proposals) {
			String content = contentProposal.getContent();
			if (content.startsWith(contents)) {
				matchingProposals.add(contentProposal);
			}
		}
		return (IContentProposal[]) matchingProposals
				.toArray(new IContentProposal[matchingProposals.size()]);
	}

	private static class CronContentProposal extends ContentProposal {

		public CronContentProposal(String content, String description) {
			super(content, content, description);
		}

	}

}
