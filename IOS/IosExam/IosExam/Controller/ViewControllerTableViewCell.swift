//
//  ViewControllerTableViewCell.swift
//  IosExam
//
//  Created by SATABHISHA ROY on 01/01/21.
//

import UIKit

class ViewControllerTableViewCell: UITableViewCell {

    @IBOutlet weak var labelTitle: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
