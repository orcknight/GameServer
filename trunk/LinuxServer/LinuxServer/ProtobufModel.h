#pragma once

#include <string>

using std::string;


class ProtobufModel
{
public:
	ProtobufModel() {}

private:
	unsigned char m_type;
	unsigned int m_length;
	string m_json_data;
};

