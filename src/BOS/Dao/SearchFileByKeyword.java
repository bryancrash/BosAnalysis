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

public class SearchFileByKeyword {
	public class CheckCss extends RecursiveAction {
		private static final int MAX = 3;
		File[] file;
		private final int start;
		private final int end;
		String type;
		String keyword;

		public CheckCss(File[] file, int start, int end, String type,
				String keyword) {
			this.file = file;
			this.start = start;
			this.end = end;
			this.type = type;
			this.keyword = keyword;
		}

		@Override
		protected void compute() {
			if ((end - start) < MAX) {
				// System.out.println(start+"................"+end);
				for (int i = start; i < end; i++) {
					String companyId = file[i].getName();
					lookupfiles(file[i], type, keyword, companyId);
				}
			} else {
				// 将大任务分解成两个小任务
				int mid = (start + end) / 2;
				CheckCss left = new CheckCss(file, start, mid, type, keyword);
				CheckCss right = new CheckCss(file, mid, end, type, keyword);
				// 并行执行两个小任务
				left.fork();
				right.fork();
			}
		}

		public void lookupfiles(File file, String type, String keyword,
				String companyId) {
			File[] fs = file.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					try {
						lookupfiles(fs[i], type, keyword, companyId);
					} catch (Exception e) {
					}
				} else {
					String s=fs[i].getAbsoluteFile().toString();
					Pattern r = Pattern.compile(".*" + type + "*.");
					Matcher m = r.matcher(s);
					if (m.find()) {
						// System.out.println(fs[i]);
						getFileContent(fs[i], keyword, companyId);
					}
				}
			}
		}

		private void getFileContent(File file, String keyword, String companyId) {
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
				  if (s.indexOf(keyword) > 0) {
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
	}

	List<SearchResult> resultList = new ArrayList<SearchResult>();

	public List<SearchResult> getResultList() {
		return resultList;
	}

	public List<SearchResult> SearchKeyWord(File file, String type,
			String keyword) throws Exception {
		File[] child = file.listFiles();
		int filelen = child.length;
		SearchFileByKeyword sf = new SearchFileByKeyword();
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CheckCss check = sf.new CheckCss(child, 0, filelen, type, keyword);
		forkJoinPool.submit(check);
		forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);

		// 中所有的任务都执行结束
		List<SearchResult> resultList = sf.getResultList();
		return resultList;
	}
}
