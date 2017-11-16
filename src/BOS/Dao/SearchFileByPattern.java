package BOS.Dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BOS.Result.SearchResult;

public class SearchFileByPattern {
	public class CheckFile extends RecursiveAction {
		private static final int MAX = 3;
		File[] file;
		private final int start;
		private final int end;
		String type;
		String common;

		public CheckFile(File[] file, int start, int end, String type,
				String common) {
			this.file = file;
			this.start = start;
			this.end = end;
			this.type = type;
			this.common = common;
		}

		@Override
		protected void compute() {
			if ((end - start) < MAX) {
				// System.out.println(start+"................"+end);
				for (int i = start; i < end; i++) {
					String companyId = file[i].getName();
					lookupfiles(file[i], type, common, companyId);
				}
			} else {
				// 将大任务分解成两个小任务
				int mid = (start + end) / 2;
				CheckFile left = new CheckFile(file, start, mid, type, common);
				CheckFile right = new CheckFile(file, mid, end, type, common);
				// 并行执行两个小任务
				left.fork();
				right.fork();
			}

		}

		public void lookupfiles(File file, String type, String common,
				String companyId) {
			File[] fs = file.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					try {
						lookupfiles(fs[i], type, common, companyId);
					} catch (Exception e) {
					}
				} else {
					String s=fs[i].getAbsoluteFile().toString();
					Pattern r = Pattern.compile(".*" + type + "*.");
					Matcher m = r.matcher(s);
					if (m.find()) {
						// System.out.println(fs[i]);
						getFileContent(fs[i], common, companyId);
					}
				}
			}
		}

	}

	private void getFileContent(File file, String common, String companyId) {
		String fileName = file.getAbsoluteFile().toString();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = null;
			int i = 0;
			String lastLine = "";
			while ((s = br.readLine()) != null) {
				i++;
				Pattern r = Pattern.compile(".*" + common + "*.");
				Matcher m = r.matcher(s);
				if (m.find()) {
					SearchResult sr = new SearchResult(); 
					sr.setCompanyId(companyId);
					sr.setFileName(fileName);
					sr.setLineNumber(i);
					sr.setContent(s);
					resultList.add(sr);
				}
			}
			// System.out.println(resultList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	List<SearchResult> resultList = new ArrayList<SearchResult>();

	public List<SearchResult> getResultList() {
		return resultList;
	}

	public List<SearchResult> SearchPattern(File file, String type,
			String common) throws Exception {
		File[] child = file.listFiles();
		int filelen = child.length;
		SearchFileByPattern sfp = new SearchFileByPattern();
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CheckFile check = sfp.new CheckFile(child, 0, filelen, type, common);
		forkJoinPool.submit(check);
		forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);

		// 中所有的任务都执行结束
		List<SearchResult> resultList = sfp.getResultList();
		return resultList;
	}

}
