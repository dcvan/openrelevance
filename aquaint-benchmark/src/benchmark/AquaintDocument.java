package benchmark;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class AquaintDocument {
		
		@Field("docno")
		private String docno;
		
		@Field("txttype")
		private String txttype;
		
		@Field("doctype")
 		private String doctype;
		
		@Field("datetime")
		private Date datetime;
		
		@Field("body")
		private String body;
		
		@Field("header")
		private String header;
		
		@Field("slug")
		private String slug;
		
		@Field("category")
		private String category;
		
		@Field("headline")
		private String headline;
		
		@Field("text")
		private String text;
		
		@Field("subhead")
		private String subhead;
		
		@Field("annotation")
		private String annotation;
		
		@Field("trailer")
		private String trailer;
		
		public String getDocno() {
			return docno;
		}
		public void setDocno(String docno) {
			this.docno = docno;
		}
		public String getDoctype() {
			return doctype;
		}
		public void setDoctype(String doctype) {
			this.doctype = doctype;
		}
		public Date getDatetime() {
			return datetime;
		}
		public void setDatetime(Date datetime) {
			this.datetime = datetime;
		}
		public String getHeader() {
			return header;
		}
		public void setHeader(String header) {
			this.header = header;
		}
		public String getSlug() {
			return slug;
		}
		public void setSlug(String slug) {
			this.slug = slug;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getHeadline() {
			return headline;
		}
		public void setHeadline(String headline) {
			this.headline = headline;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getTrailer() {
			return trailer;
		}
		public void setTrailer(String trailer) {
			this.trailer = trailer;
		}
		public String getTxttype() {
			return txttype;
		}
		public void setTxttype(String txttype) {
			this.txttype = txttype;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getSubhead() {
			return subhead;
		}
		public void setSubhead(String subhead) {
			this.subhead = subhead;
		}
		public String getAnnotation() {
			return annotation;
		}
		public void setAnnotation(String annotation) {
			this.annotation = annotation;
		}
		
}
