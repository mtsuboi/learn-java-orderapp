package com.example.orderapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.orderapp.constants.OrderStatus;
import com.example.orderapp.form.OrderForm;
import com.example.orderapp.logic.OrderLogic;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/orderReport")
public class OrderReportController {

	@Autowired
	ResourceLoader resource;
	
	@Autowired
	OrderLogic orderLogic;
	
	@GetMapping("/deliveryNoteByOrderId")
	public String deliveryNoteByOrderId(@RequestParam("orderId") String orderId, HttpServletResponse response, Model model) {
		// 受注を読み込み
		Optional<OrderForm> optOrderForm = orderLogic.findById(orderId);
		if(optOrderForm.isEmpty()) {
			// 該当受注が検索できなかった場合エラーページへ
			model.addAttribute("message", "指定された受注は存在しません。別のユーザーに削除された可能性があります。");
			return "error";
		}
		List<OrderForm> orderList = new ArrayList<OrderForm>();
		orderList.add(optOrderForm.get());
		
		// 帳票のPDFを取得
		byte[] bytes = getReport(orderList);
		
		// PDFを出力
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		OutputStream out;
		try {
			out = response.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/deliveryNoteByOrderStatus")
	public String deliveryNoteByOrderStatus(@RequestParam("orderStatus") String orderStatusCode, HttpServletResponse response, Model model) {
		// 受注を読み込み
		List<OrderForm> orderList = orderLogic.findByStatus(OrderStatus.getByCode(orderStatusCode), true);
		if(orderList.isEmpty()) {
			// 該当受注が検索できなかった場合エラーページへ
			model.addAttribute("message", "指定された受注は存在しません。別のユーザーに削除された可能性があります。");
			return "error";
		}
		
		// 帳票のPDFを取得
		byte[] bytes = getReport(orderList);
		
		// PDFを出力
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		OutputStream out;
		try {
			out = response.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getReport(List<OrderForm> orderList) {
		try {
			// 帳票レイアウトをコンパイル
			JasperReport jasperReport = JasperCompileManager.compileReport(resource.getResource("classpath:reports/DeliveryNote.jrxml").getURL().openStream());

			// 帳票を作成
			ListIterator<OrderForm> orderListItr = orderList.listIterator();
			OrderForm orderForm = orderListItr.next();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, makeParams(orderForm), new JRBeanCollectionDataSource(orderForm.getOrderDetailList()));

			// 2ページ目以降を追加
			while(orderListItr.hasNext()) {
				orderForm = orderListItr.next();
				List<JRPrintPage> pages = JasperFillManager.fillReport(jasperReport, makeParams(orderForm), new JRBeanCollectionDataSource(orderForm.getOrderDetailList())).getPages();
				pages.forEach(page -> jasperPrint.addPage(page));
			}

			// PDFにして返却
			return JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<String, Object> makeParams(OrderForm orderForm) {
		// 帳票パラメータ（ヘッダ部）の作成
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("PublishDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		params.put("OrderId",orderForm.getOrderId());
		params.put("CustomerName", orderForm.getCustomerName());
		params.put("OrderAmount", orderForm.getOrderAmount());
		String customerZipcode = orderForm.getCustomerZipcode();
		params.put("CustomerZipcode", "〒" + customerZipcode.substring(0, 3) + "-" + customerZipcode.substring(3));
		params.put("CustomerAddress", orderForm.getCustomerAddress());
		try {
			InputStream stampImage = resource.getResource("classpath:reports/stamp.png").getURL().openStream();
			params.put("StampImage", stampImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

}
