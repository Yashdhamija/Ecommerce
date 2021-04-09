package security;

public class HtmlFilter {
	
	
	
	
	
	
	public static String removeHTMLTags(String htmlTag) {

		String stylePattern = "<style([\\s\\S]+?)</style>";
		String scriptPattern = "<script([\\s\\S]+?)</script>";
		String htmlPattern = "<[^>]*>";
		String newHTMLTag = null;
		if (htmlTag.matches(stylePattern)) {

			newHTMLTag =htmlTag.replaceAll("<style([\\s\\S]+?)</style>", "");
		}

		if (htmlTag.matches(scriptPattern)) {

			newHTMLTag = htmlTag.replaceAll("<script([\\s\\S]+?)</script>", "");
		}

		if (htmlTag.matches(htmlPattern)) {

			newHTMLTag= htmlTag.replaceAll("<[^>]*>", "");
		}
		return newHTMLTag;

	}

	public static String replaceHTMLTags(String htmlTag) {

		String htmlPattern = "<";
		String newHTML = null;

		if (htmlTag.matches(htmlPattern)) {

			newHTML = htmlTag.replaceAll("<", "&lt");
		}

		return newHTML;

	}
	
	
	
	
	

}
