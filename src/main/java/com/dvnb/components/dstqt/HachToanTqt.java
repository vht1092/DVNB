package com.dvnb.components.dstqt;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


/**
 * Man hinh thong ke chung
 */
@SpringComponent
@ViewScope
public class HachToanTqt extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HachToanTqt.class);
	public static final String CAPTION = "HẠCH TOÁN TQT";
	private final Label lbLatestLogin = new Label();
	private final Label lbTotalAssignedCases = new Label();
	private String sUserId = "";
	private String CheckUserId = "1";
	private String roleDescription = "";
	private String note = "";
	private String fileNameImport;
	final TimeConverter timeConverter = new TimeConverter();
	private SpringConfigurationValueHelper configurationHelper;
	private File fileImport = null;
	private final VerticalLayout mainLayout = new VerticalLayout();
	
	public HachToanTqt() {
		setCaption(CAPTION);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		this.sUserId = SecurityUtils.getUserId();
		
		final SimpleDateFormat simpledateformat_current = new SimpleDateFormat("dd/M/yyyy");

		Upload chooseFile = new Upload(null, new Upload.Receiver() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream outputFile = null;
				try {
					// TODO Auto-generated method stub
					fileNameImport = StringUtils.substringBefore(filename, ".xlsx") + "_" + timeConverter.getCurrentTime() + ".xlsx";

					Window confirmDialog = new Window();
					final FormLayout content = new FormLayout();
					content.setMargin(true);

					Button bYes = new Button("OK");

					confirmDialog.setCaption("Chương trình sẽ tạo hàng loạt nhiều thẻ, bấm OK để tiếp tục");
					confirmDialog.setWidth(400.0f, Unit.PIXELS);
					try {
						if (!filename.isEmpty()) {
							fileImport = new File(configurationHelper.getPathFileRoot() + "/" + fileNameImport);
							if (!fileImport.exists()) {
								fileImport.createNewFile();
							}
							outputFile = new FileOutputStream(fileImport);

							bYes.addClickListener(event -> {
								try {
									InputStream is = null;
									is = new FileInputStream(fileImport);

									
									
									confirmDialog.close();
									
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
									LOGGER.error(e.getMessage());
								}

							});

							// -----------------
							VerticalLayout layoutBtn = new VerticalLayout();
							layoutBtn.addComponents(bYes);
							layoutBtn.setComponentAlignment(bYes, Alignment.BOTTOM_CENTER);
							content.addComponent(layoutBtn);

							confirmDialog.setContent(content);

							getUI().addWindow(confirmDialog);

							// Center it in the browser window
							confirmDialog.center();
							confirmDialog.setResizable(false);
						} else
							outputFile = null;

					} catch (IOException e) {
						e.printStackTrace();
						LOGGER.error("Error: " + e.getMessage());
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					LOGGER.error(e.toString());
				}
				return outputFile;

			}

		});
		chooseFile.setButtonCaption("Create");
		chooseFile.addStyleName("myCustomUpload");

		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(chooseFile);
		
		setCompositionRoot(mainLayout);
	}


	private static int getRandomSeq(int min, int max) {
		// It has a minimum value of -2,147,483,648 and a maximum value of 2,147,483,647 (inclusive)
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
	public static boolean isCellEmpty(final Cell cell) {
	    if (cell == null) { // use row.getCell(x, Row.CREATE_NULL_AS_BLANK) to avoid null cells
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().trim().isEmpty()) {
	        return true;
	    }

	    return false;
	}
	
	 public static String deAccent(String str) {
         String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
         Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
         return pattern.matcher(nfdNormalizedString).replaceAll("").replace("đ", "d").replaceAll("Đ", "D");
     }
	 

	@Override
	public void eventReload() {
		
	}
}
