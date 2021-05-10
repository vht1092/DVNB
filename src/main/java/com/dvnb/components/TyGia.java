package com.dvnb.components;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Scope;

import com.dvnb.ReloadAutoComponent;
import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.SysUserroleService;
import com.dvnb.services.TyGiaService;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
/**
 * tanvh1 Aug 20, 2019
 *
 */
@SpringComponent
@Scope("prototype")
public class TyGia extends CustomComponent implements ReloadAutoComponent, ReloadComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CAPTION = "TỶ GIÁ";
	private static final String CARD_BRN = "LOẠI THẺ";
	private static final String KY = "KỲ";
	
	
	public final transient FormLayout formLayout = new FormLayout();
	
	
	private final SysUserroleService sysUserroleService;
	private TyGiaService tyGiaService;
	
	public final transient Grid gridContent;
	public final transient IndexedContainer containerContent;
	
//	public final transient TextField tfNgayTyGia;
	public final transient ComboBox cbbKy;
	public final transient ComboBox cbbCardbrn;
	private final TextField tfTyGia;
	private Window confirmDialog = new Window();
	private Button bYes;
	private Button bNo;
	private transient String sUserId;
	private String CheckUserId = "";
	final TimeConverter timeConverter = new TimeConverter();
	
	public TyGia() {
		//TAN 20190815
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		tyGiaService = (TyGiaService) helper.getBean("tyGiaService");
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		sysUserroleService = (SysUserroleService) helper.getBean("sysUserroleService");
		
		this.sUserId = SecurityUtils.getUserId();
		CheckUserId = sysUserroleService.findByRoleId(sUserId);
		
		cbbKy = new ComboBox(KY);
		cbbKy.setNullSelectionAllowed(false);
		cbbKy.setWidth("20%");
		descService.findAllByTypeByOrderBySequencenoAsc("KYBAOCAO").forEach(item -> {
			cbbKy.addItem(item.getId());
			cbbKy.setItemCaption(item.getId(),item.getDescription());
		});
		
		Collection<?> itemContents = cbbKy.getItemIds();
		cbbKy.setValue(itemContents.iterator().next());
        
		cbbCardbrn = new ComboBox(CARD_BRN);
		cbbCardbrn.setNullSelectionAllowed(false);
		cbbCardbrn.setWidth("20%");
		cbbCardbrn.addItems("MC","VS");
		
//		tfNgayTyGia = new TextField("NGÀY TỶ GIÁ");
//		tfNgayTyGia.setSizeFull();
//		tfNgayTyGia.setWidth("20%");
		
		DateField dateTyGia = new DateField("NGÀY TỈ GIÁ");
		dateTyGia.setDateFormat("dd/MM/yyyy");
		dateTyGia.setWidth("20%");
		
		tfTyGia = new TextField("TỶ GIÁ");
		tfTyGia.setWidth("20%");
		
		final Button btnSave = new Button("Lưu");
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.setStyleName(ValoTheme.BUTTON_LARGE);
		
		gridContent = new Grid();
		gridContent.setSizeFull();
		gridContent.setHeightByRows(10);
		gridContent.setHeightMode(HeightMode.ROW);
		gridContent.setEditorEnabled(true);
		
		containerContent = new IndexedContainer();
		containerContent.addContainerProperty("id", String.class, "");
		containerContent.addContainerProperty("cretms", String.class, "");
		containerContent.addContainerProperty("usrid", String.class, "");
		containerContent.addContainerProperty("updtms", String.class, "");
		containerContent.addContainerProperty("upduid", String.class, "");
		containerContent.addContainerProperty("ky", String.class, "");
		containerContent.addContainerProperty("crdbrn", String.class, "");
		containerContent.addContainerProperty("ngayTyGia", String.class, "");
		containerContent.addContainerProperty("tyGia", String.class, "");
		
		gridContent.setContainerDataSource(containerContent);
		gridContent.getColumn("id").setHidden(true);
		gridContent.getColumn("cretms").setHidden(true);
		gridContent.getColumn("usrid").setHidden(true);
		gridContent.getColumn("updtms").setHidden(true);
		gridContent.getColumn("upduid").setHidden(true);
		gridContent.getColumn("ky").setHeaderCaption("KỲ");
		gridContent.getColumn("ky").setEditable(false);
		gridContent.getColumn("crdbrn").setHeaderCaption("LOẠI THẺ");
		gridContent.getColumn("crdbrn").setEditable(false);
		gridContent.getColumn("ngayTyGia").setHeaderCaption("NGÀY TỶ GIÁ");
		gridContent.getColumn("ngayTyGia").setEditable(false);
		gridContent.getColumn("tyGia").setHeaderCaption("TỶ GIÁ");
		
		refreshData();
		
		cbbKy.addValueChangeListener(event -> {
//			String date = "01" + event.getProperty().getValue();
//			LocalDate lastDayOfMonth = LocalDate.parse(date,DateTimeFormatter.ofPattern("ddMMyyyy"))
//			                               .with(TemporalAdjusters.lastDayOfMonth());
//			try {
//				dateTyGia.setValue(new SimpleDateFormat("dd/MM/yyyy").parse(lastDayOfMonth.toString()));
//			} catch (ReadOnlyException | ConversionException | ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			refreshData();
			
		});
		
		gridContent.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				
				try {
					DvnbTyGia dvnbTygia = new DvnbTyGia();
					dvnbTygia.setId(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "id").toString());
					dvnbTygia.setCreTms(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "cretms").toString());
					dvnbTygia.setUsrId(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "usrid").toString());
					dvnbTygia.setUpdTms(timeConverter.getCurrentTime());
					dvnbTygia.setUpdUid(sUserId);
					dvnbTygia.setKy(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "ky").toString());
					dvnbTygia.setCrdBrn(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "crdbrn").toString());
					dvnbTygia.setNgayTyGia(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "ngayTyGia").toString());
					dvnbTygia.setTyGia(new BigDecimal(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "tyGia").toString().replaceAll(",", "")));
					tyGiaService.save(dvnbTygia);
					Notification.show("Tỷ giá kỳ " + dvnbTygia.getKy() + " đã được cập nhật.");
				} catch (Exception e) {
					Notification.show("Lỗi ứng dụng: "+ e.getMessage(), Type.ERROR_MESSAGE);
				}
			}});
		
		btnSave.addClickListener(event -> {
			tfTyGia.addValidator(new StringLengthValidator("Plase enter content detail", 1, 250, false));
			if(!tfTyGia.equals(null) && !tfTyGia.isEmpty()) {
				try {
					DvnbTyGia dvnbTygia = new DvnbTyGia();
					dvnbTygia.setId(cbbCardbrn.getValue().toString() + cbbKy.getValue().toString());
					dvnbTygia.setCreTms(timeConverter.getCurrentTime());
					dvnbTygia.setUsrId(sUserId);
					dvnbTygia.setKy(cbbKy.getValue().toString());
					dvnbTygia.setCrdBrn(cbbCardbrn.getValue().toString());
					dvnbTygia.setNgayTyGia(new SimpleDateFormat("dd/MM/yyyy").format(dateTyGia.getValue()));
					dvnbTygia.setTyGia(new BigDecimal(tfTyGia.getValue().replaceAll(",", "")));
					tyGiaService.save(dvnbTygia);
					Notification.show("Thêm tỷ giá của kỳ " + dvnbTygia.getKy() + " thành công.", Type.WARNING_MESSAGE);
				} catch (Exception e) {
					Notification.show("Lỗi ứng dụng: "+ e.getMessage(), Type.ERROR_MESSAGE);
				}
				refreshData();
			}
			
			
		});
		
		
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		final HorizontalLayout hBodyLayout = new HorizontalLayout();
		hBodyLayout.setSizeFull();
		hBodyLayout.setMargin(true);
		hBodyLayout.setSpacing(true);

		formLayout.addComponent(cbbKy);
		formLayout.addComponent(cbbCardbrn);
		formLayout.addComponent(dateTyGia);
		formLayout.addComponent(tfTyGia);
		formLayout.addComponent(btnSave);
		
		hBodyLayout.addComponent(formLayout);
		
		
		mainLayout.addComponent(hBodyLayout);
		mainLayout.addComponent(gridContent);
		
		setCompositionRoot(mainLayout);
		
		
	}
	
	@Override
	public void eventReload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventReloadAuto() {
		// TODO Auto-generated method stub
		
	}
	
	private void refreshData() {
		final Iterable<DvnbTyGia> listTygiaIter = tyGiaService.findAllByKy(cbbKy.getValue().toString());
		List<DvnbTyGia> listTygia = StreamSupport.stream(listTygiaIter.spliterator(), false).sorted((o1, o2)->o1.getNgayTyGia().compareTo(o2.getNgayTyGia())).collect(Collectors.toList());
		
		if (!listTygia.isEmpty()) {
			if (!containerContent.getItemIds().isEmpty()) {
				containerContent.removeAllItems();
			}
			for (int i = 0; i <= listTygia.size() - 1; i++) {
				Item item = containerContent.getItem(containerContent.addItem());
				item.getItemProperty("id").setValue(listTygia.get(i).getId() != null ? listTygia.get(i).getId().toString() : "");
				item.getItemProperty("cretms").setValue(listTygia.get(i).getCreTms() != null ? listTygia.get(i).getCreTms().toString() : "");
				item.getItemProperty("usrid").setValue(listTygia.get(i).getUsrId() != null ? listTygia.get(i).getUsrId().toString() : "");
				item.getItemProperty("updtms").setValue(listTygia.get(i).getUpdTms() != null ? listTygia.get(i).getUpdTms().toString() : "");
				item.getItemProperty("upduid").setValue(listTygia.get(i).getUpdUid() != null ? listTygia.get(i).getUpdUid().toString() : "");
				item.getItemProperty("ky").setValue(listTygia.get(i).getKy() != null ? listTygia.get(i).getKy().toString() : "");
				item.getItemProperty("crdbrn").setValue(listTygia.get(i).getKy() != null ? listTygia.get(i).getCrdBrn().toString() : "");
				item.getItemProperty("ngayTyGia").setValue(listTygia.get(i).getNgayTyGia() != null ? listTygia.get(i).getNgayTyGia().toString() : "");
				DecimalFormat tyGiaFormat = new DecimalFormat("#,###.00000");
				item.getItemProperty("tyGia").setValue(tyGiaFormat.format(listTygia.get(i).getTyGia() != null ? listTygia.get(i).getTyGia() : BigDecimal.ZERO));
			}
		}
		else
			containerContent.removeAllItems();
	}

}
