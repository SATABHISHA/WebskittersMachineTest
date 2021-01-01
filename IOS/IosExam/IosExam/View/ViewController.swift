//
//  ViewController.swift
//  IosExam
//
//  Created by SATABHISHA ROY on 01/01/21.
//

import UIKit
import Alamofire
import SwiftyJSON

struct cellData{
    var Objective:String!
    var Keydata = [[String:AnyObject]]()
}
class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var viewControllerTableview: UITableView!
    
    var arrRes = [[String:AnyObject]]()
    var arrResChild = [[String:AnyObject]]()
    var tableViewData = [cellData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        viewControllerTableview.delegate = self
        viewControllerTableview.dataSource = self
        
       
        loadData()
    }
    
    //---------tableview code starts-------
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
//        return 60
        var returnValue:CGFloat!
        if tableView.tag == 100{
            returnValue = 60
        }else{
            returnValue = 0
        }
        return returnValue
    }
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        var dict = arrResChild[section]
        let view = UIView()
        view.backgroundColor = UIColor(hexFromString: "#2e5772")
        
        let objectTitle = UILabel()
        objectTitle.text = dict["content_obj"] as? String
        objectTitle.numberOfLines = 1;
        objectTitle.font = UIFont.boldSystemFont(ofSize: 22)
        objectTitle.textColor = UIColor(hexFromString: "#ffffff")
        objectTitle.adjustsFontSizeToFitWidth = true;
        objectTitle.frame = CGRect(x:13, y:5, width: objectTitle.intrinsicContentSize.width, height: 35)
        view.addSubview(objectTitle)
        
        
        return view
    }
   
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return tableViewData.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableViewData[section].Keydata.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! ViewControllerTableViewCell
        let dict = tableViewData[indexPath.section].Keydata[indexPath.row]
        cell.labelTitle.text = dict["key_result"] as? String
            
        return cell
    }
    //--------tableview code ends------
    
    
    func loadData(){
       
        
        let url = BASE_URL
        print("url-=>",url)
        
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(token_string)",
            "Accept": "application/json"
        ]
        
        AF.request(url, headers: headers).responseJSON { response in
            //            print(response)
            if((response.value) != nil){
                let swiftyJsonVar=JSON(response.value!)
                print("Data: \(swiftyJsonVar)")
                
                if let resData = swiftyJsonVar["data"].arrayObject{
                    self.arrRes = resData as! [[String:AnyObject]]
                }
                if let resDataObject = swiftyJsonVar["data"][0]["planning"]["objective"].arrayObject{
                    self.arrResChild = resDataObject as! [[String:AnyObject]]
                }
                print("resDataObject-=>", self.arrResChild.count)
                if(!self.tableViewData.isEmpty){
                    self.tableViewData.removeAll()
                }
                
                var getData = [String:AnyObject]()
                for i in 0..<swiftyJsonVar["data"][0]["planning"]["objective"].count{
                    var k = cellData()
                    var getData1 = [Any]()
                    k.Objective = swiftyJsonVar["data"][0]["planning"]["objective"][i]["content_obj"].stringValue
                    for j in 0..<swiftyJsonVar["data"][0]["planning"]["objective"][i]["key_result"].count{
                        getData.updateValue(swiftyJsonVar["data"][0]["planning"]["objective"][i]["key_result"][j]["key_result"].stringValue as AnyObject, forKey: "key_result")
                        getData1.append(getData)
                    }
                    k.Keydata = getData1 as! [[String:AnyObject]]
                    self.tableViewData.append(k)
                   
                }
                
                print("tablecount-=>",self.tableViewData.count)
                
                if self.arrRes.count>0 {
                    self.viewControllerTableview.reloadData()
                }
            
        }
        
    }
    
}
}
//-----------hexacode color conversion, code starts---------
extension UIColor {
    convenience init(hexFromString:String, alpha:CGFloat = 1.0) {
        var cString:String = hexFromString.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        var rgbValue:UInt32 = 10066329 //color #999999 if string has wrong format
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.count) == 6) {
            Scanner(string: cString).scanHexInt32(&rgbValue)
        }
        
        self.init(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: alpha
        )
    }
}
//-----------hexacode color conversion, code ends---------
