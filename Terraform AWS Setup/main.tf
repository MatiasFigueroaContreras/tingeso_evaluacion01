terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws" # Indica el servicio del proveedor a utilizar
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "sa-east-1"
}

data "aws_ami" "linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm*"]
  }

}

resource "aws_instance" "milkstgo_server" {
  ami           = data.aws_ami.linux.id # ami-09689ccfc75aa1484
  instance_type = "t2.micro"
  key_name      = "milkstgo-key-pair" # Key pair creada en AWS
  user_data     = "${file("setup.sh")}" # Archivo a ejecutar con la configuracion inicial
  tags = {
    Name = "MilkStgoInstance"
  }
}

output "public_ip" {
  value = aws_instance.milkstgo_server.public_ip
}
